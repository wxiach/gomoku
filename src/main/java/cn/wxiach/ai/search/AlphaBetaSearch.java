package cn.wxiach.ai.search;

import cn.wxiach.ai.evaluate.GomokuEvaluator;
import cn.wxiach.ai.pattern.GomokuShapeDetector;
import cn.wxiach.ai.pattern.Pattern;
import cn.wxiach.ai.pattern.PatternCollection;
import cn.wxiach.core.rule.BoardChecker;
import cn.wxiach.core.rule.WinArbiter;
import cn.wxiach.core.utils.BoardUtils;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;

import java.util.*;
import java.util.function.Consumer;

public class AlphaBetaSearch {
    private static final GomokuEvaluator evaluator = new GomokuEvaluator();
    private final TranspositionTable transpositionTable;
    private final ZobristHash zobristHash;
    private final CandidateSearch candidateSearch = new CandidateSearch();

    private BoardWithZobrist board;
    private int depth;
    private Stone result;

    public AlphaBetaSearch(ZobristHash zobristHash, TranspositionTable transpositionTable) {
        this.zobristHash = zobristHash;
        this.transpositionTable = transpositionTable;
    }

    public int execute(Board board, Color color, int depth, Consumer<Object> handler) {
        this.board = new BoardWithZobrist(board, zobristHash);
        this.depth = depth;
        /*
         * Negamax search inverts alpha and beta during recursion.
         * To prevent integer overflow:
         * - alpha is offset by +10,000 from Integer.MIN_VALUE.
         * - beta is offset by -10,000 from Integer.MAX_VALUE.
         */
        int value = alphaBeta(depth, Integer.MIN_VALUE + 10000, Integer.MAX_VALUE - 10000, color);
        handler.accept(result);
        return value;
    }


    /**
     * @param depth
     * @param alpha
     * @param beta
     * @param color
     * @return
     */
    public int alphaBeta(int depth, int alpha, int beta, Color color) {
        Integer evaluation = transpositionTable.find(board.hash(), depth, alpha, beta, color);
        if (evaluation != null && depth != this.depth) {
            return evaluation;
        }

        if (depth == 0 || WinArbiter.checkOver(this.board)) {
            return evaluator.evaluate(board, color);
        }

        int evaluationType = TranspositionTable.Entry.ALPHA;

        for (Stone stone : candidateSearch.obtainCandidates(this.board, color)) {

            this.board.makeMove(stone);
            int value = -alphaBeta(depth - 1, -beta, -alpha, Color.reverse(color));
            this.board.undoMove(stone);

            if (value >= beta) {
                transpositionTable.store(board.hash(), beta, TranspositionTable.Entry.BETA, depth, color);
                return beta;
            }
            if (value > alpha) {
                alpha = value;
                evaluationType = TranspositionTable.Entry.EXACT;
                if (depth == this.depth) {
                    result = stone;
                }
            }
        }

        transpositionTable.store(board.hash(), alpha, evaluationType, depth, color);

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

            // Detect threat stone
            Board opponentBoard = board;
            Color opponentColor = Color.reverse(color);
            if (color == Color.BLACK) {
                opponentColor = Color.BLACK;
                opponentBoard = BoardUtils.reverseStoneColorOnBoard(board.copy());
            }
            for (Point point : surroundBlankPoint) {
                Stone stone = Stone.of(point, opponentColor);
                opponentBoard.makeMove(stone);
                Collection<Pattern> patterns = GomokuShapeDetector.getInstance().detect(opponentBoard, point);
                opponentBoard.undoMove(stone);
                if (patterns.contains(PatternCollection.FIVE)) return List.of(Stone.of(point, color));
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

            return candidateScoreMap.entrySet().stream().sorted(comparator).map(Map.Entry::getKey).limit(10).toList();

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
                    if (BoardChecker.isEmpty(board, candidatePoint)) blankPoints.add(candidatePoint);
                }
            }
            return blankPoints;
        }
    }

}
