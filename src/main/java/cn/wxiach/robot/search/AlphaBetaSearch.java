package cn.wxiach.robot.search;

import cn.wxiach.gomoku.rule.BoardCheck;
import cn.wxiach.gomoku.rule.WinConditionCheck;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;
import cn.wxiach.robot.evaluation.GomokuEvaluator;
import cn.wxiach.robot.support.BoardWithZobrist;
import cn.wxiach.robot.support.TranspositionTable;
import cn.wxiach.robot.support.ZobristHash;
import cn.wxiach.utils.BoardUtils;

import java.util.*;
import java.util.function.Consumer;

/**
 * Parallel Alpha-Beta search for Gomoku.
 */
public class AlphaBetaSearch {

    private static final GomokuEvaluator evaluator = new GomokuEvaluator();

    private final TranspositionTable transpositionTable;
    private final ZobristHash zobristHash;
    private final CandidateSearch candidateSearch = new CandidateSearch();

    private record SearchResult(Stone move, int score) {
    }

    public AlphaBetaSearch(ZobristHash zobristHash, TranspositionTable transpositionTable) {
        this.zobristHash = zobristHash;
        this.transpositionTable = transpositionTable;
    }

    /**
     * Executes the search to find the best move using parallel streams.
     */
    public int execute(Board initialBoard, Color color, int depth, Consumer<Object> handler) {
        BoardWithZobrist board = new BoardWithZobrist(initialBoard, zobristHash);
        List<Stone> candidateMoves = candidateSearch.obtainCandidates(board, color);

        int alpha = Integer.MIN_VALUE + 1000;
        int beta = Integer.MAX_VALUE - 1000;

        SearchResult result = candidateMoves.parallelStream()
                .map(move -> {
                    BoardWithZobrist boardCopy = new BoardWithZobrist(board.copy(), this.zobristHash);
                    boardCopy.makeMove(move);
                    int score = -search(boardCopy, depth - 1, -beta, -alpha, Color.reverse(color));
                    return new SearchResult(move, score);
                })
                .max(Comparator.comparingInt(SearchResult::score))
                .orElseGet(() -> new SearchResult(candidateMoves.getFirst(), Integer.MIN_VALUE));

        handler.accept(result.move());
        return result.score();
    }

    private int search(BoardWithZobrist board, int depth, int alpha, int beta, Color color) {
        long currentHash = board.hash();
        Integer evaluation = transpositionTable.find(currentHash, depth, alpha, beta, color);
        if (evaluation != null) {
            return evaluation;
        }

        if (depth == 0 || WinConditionCheck.checkOver(board)) {
            return evaluator.evaluate(board, color);
        }

        int evaluationType = TranspositionTable.Entry.ALPHA;

        for (Stone stone : candidateSearch.obtainCandidates(board, color)) {
            board.makeMove(stone);
            int value = -search(board, depth - 1, -beta, -alpha, Color.reverse(color));
            board.undoMove(stone);

            if (value >= beta) {
                transpositionTable.store(currentHash, beta, TranspositionTable.Entry.BETA, depth, color);
                return beta;
            }
            if (value > alpha) {
                alpha = value;
                evaluationType = TranspositionTable.Entry.EXACT;
            }
        }

        transpositionTable.store(currentHash, alpha, evaluationType, depth, color);

        return alpha;
    }

    public static class CandidateSearch {

        public List<Stone> obtainCandidates(Board board, Color color) {
            int boardStoneCount = BoardUtils.countStones(board);
            Set<Point> surroundBlankPoint = new HashSet<>();
            for (int i = 0; i < board.length(); i++) {
                if (board.color(i) == Color.EMPTY) continue;
                surroundBlankPoint.addAll(
                        searchSurroundBlankPoint(board, (board.point(i)), boardStoneCount <= 4 ? 1 : 2));
            }

            Map<Stone, Integer> candidateScoreMap = new HashMap<>();
            surroundBlankPoint.forEach(point -> {
                Stone stone = Stone.of(point, color);
                board.makeMove(stone);
                candidateScoreMap.put(stone, evaluator.evaluate(board, color));
                board.undoMove(stone);
            });

            Comparator<Map.Entry<Stone, Integer>> comparator = Comparator
                    .comparing((Map.Entry<Stone, Integer> entry) -> -entry.getValue());

            return candidateScoreMap.entrySet().stream()
                    .sorted(comparator).map(Map.Entry::getKey).limit(8).toList();
        }


        private Set<Point> searchSurroundBlankPoint(Board board, Point point, int range) {
            Set<Point> blankPoints = new HashSet<>();
            int minX = Math.max(point.x() - range, 0);
            int maxX = Math.min(point.x() + range, Board.SIZE);
            int minY = Math.max(point.y() - range, 0);
            int maxY = Math.min(point.y() + range, Board.SIZE);

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    Point candidatePoint = Point.of(x, y);
                    if (BoardCheck.isEmpty(board, candidatePoint)) {
                        blankPoints.add(candidatePoint);
                    }
                }
            }
            return blankPoints;
        }
    }
}
