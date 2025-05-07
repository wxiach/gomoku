package cn.wxiach.robot.search;

import cn.wxiach.features.pattern.Patterns;
import cn.wxiach.gomoku.rule.WinConditionCheck;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;
import cn.wxiach.robot.support.BoardWithZobrist;
import cn.wxiach.robot.support.TranspositionTable;
import cn.wxiach.robot.support.ZobristHash;
import cn.wxiach.utils.BoardUtils;
import cn.wxiach.utils.MathUtils;

import java.util.*;
import java.util.function.Consumer;

/**
 * Parallel Alpha-Beta search for Gomoku.
 */
public class AlphaBetaSearch extends CandidateSearch {

    private final TranspositionTable transpositionTable;
    private final ZobristHash zobristHash;
    private final ThreatSearch threatSearch;

    private record SearchResult(Stone move, int value) {
    }

    public AlphaBetaSearch(ZobristHash zobristHash, TranspositionTable transpositionTable) {
        this.zobristHash = zobristHash;
        this.transpositionTable = transpositionTable;
        this.threatSearch = new ThreatSearch(transpositionTable);
    }

    /**
     * Executes the search to find the best move using parallel streams.
     */
    public int execute(Board board, Color color, int depth, Consumer<Object> handler) {

        int alpha = Integer.MIN_VALUE + 1000;
        int beta = Integer.MAX_VALUE - 1000;

        List<Stone> candidateMoves = obtainCandidates(board, color);
        SearchResult result = candidateMoves.parallelStream()
                .map(move -> {
                    BoardWithZobrist boardCopy = new BoardWithZobrist(board.copy(), this.zobristHash);
                    boardCopy.makeMove(move);
                    int value = -search(boardCopy, depth - 1, -beta, -alpha, Color.reverse(color));
                    return new SearchResult(move, value);
                })
                .max(Comparator.comparingInt(SearchResult::value))
                .orElseGet(() -> new SearchResult(candidateMoves.getFirst(), Integer.MIN_VALUE));

        handler.accept(result.move());
        return result.value();
    }

    private int search(BoardWithZobrist board, int depth, int alpha, int beta, Color color) {
        Integer evaluation = transpositionTable.find(board.hash(), depth, alpha, beta, color);
        if (evaluation != null) {
            return evaluation;
        }

        if (WinConditionCheck.checkOver(board)) return evaluator.evaluate(board, color);

        if (depth == ThreatSearch.THREAT_SEARCH_DEPTH) {
            evaluation = threatSearch.search(board, depth, alpha, beta, color);
            if (MathUtils.approximateEqual(evaluation, Patterns.A5.value(), 1.2)) {
                return evaluation;
            }
            return evaluator.evaluate(board, color);
        }

        int evaluationType = TranspositionTable.Entry.ALPHA;

        for (Stone stone : obtainCandidates(board, color)) {
            board.makeMove(stone);
            int value = -search(board, depth - 1, -beta, -alpha, Color.reverse(color));
            board.undoMove(stone);

            if (value >= beta) {
                transpositionTable.store(board.hash(), beta, TranspositionTable.Entry.BETA, depth, color);
                return beta;
            }
            if (value > alpha) {
                alpha = value;
                evaluationType = TranspositionTable.Entry.EXACT;
            }
        }

        transpositionTable.store(board.hash(), alpha, evaluationType, depth, color);

        return alpha;
    }


    private List<Stone> obtainCandidates(Board board, Color color) {
        int boardStoneCount = BoardUtils.countStones(board);
        Set<Point> surroundPoints = searchSurroundBlankPoints(board, boardStoneCount <= 4 ? 1 : 2);

        Map<Stone, Integer> candidateScoreMap = new HashMap<>();
        for (Point point : surroundPoints) {
            Stone stone = Stone.of(point, color);
            board.makeMove(stone);
            candidateScoreMap.put(stone, evaluator.evaluate(board, color));
            board.undoMove(stone);
        }

        Comparator<Map.Entry<Stone, Integer>> comparator = Comparator
                .comparing((Map.Entry<Stone, Integer> entry) -> -entry.getValue());

        return candidateScoreMap.entrySet().stream()
                .sorted(comparator).limit(12).map(Map.Entry::getKey).toList();
    }

}
