package cn.wxiach.core.ai.search;

import cn.wxiach.core.ai.evaluator.Evaluator;
import cn.wxiach.core.ai.evaluator.FeatureBasedEvaluator;
import cn.wxiach.core.ai.search.support.TranspositionEntry;
import cn.wxiach.core.ai.search.support.TranspositionTable;
import cn.wxiach.core.ai.search.support.ZobristHash;
import cn.wxiach.core.state.rule.GameStateCheck;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.model.Point;

public class AlphaBetaSearch {

    public static final int DEFAULT_SEARCH_DEPTH = 4;

    private final ZobristHash zobristHash;
    private final TranspositionTable transpositionTable;

    private final CandidatePointSearch candidatePointSearch;

    private final Evaluator evaluator;

    private final char[][] board;
    private final Color color;

    private Point bestPoint;

    public AlphaBetaSearch(char[][] board, Color color, ZobristHash zobristHash, TranspositionTable transpositionTable) {
        this.board = board;
        this.color = color;
        this.candidatePointSearch = new CandidatePointSearch(board);
        this.evaluator = new FeatureBasedEvaluator();
        this.zobristHash = zobristHash;
        this.transpositionTable = transpositionTable;
    }

    public Piece execute() {
        alphaBeta(DEFAULT_SEARCH_DEPTH, Integer.MIN_VALUE + 10000, Integer.MAX_VALUE - 10000, color);
        return Piece.of(bestPoint, color);
    }


    public int alphaBeta(int depth, int alpha, int beta, Color color) {
        long hash = zobristHash.compute(board);

        Integer evaluation = transpositionTable.find(hash, depth, alpha, beta, color);
        if (evaluation != null && depth != DEFAULT_SEARCH_DEPTH) {
            return evaluation;
        }

        if (depth == 0 || GameStateCheck.isOver(board)) {
            return evaluator.evaluate(board, color);
        }

        int origAlpha = alpha;

        for (Point point : candidatePointSearch.obtainCandidatePoints(color)) {

            board[point.x()][point.y()] = color.getValue();
            hash = zobristHash.update(hash, point.x(), point.y(), color.getValue());

            int score = -alphaBeta(depth - 1, -beta, -alpha, Color.reverse(color));

            board[point.x()][point.y()] = Color.EMPTY.getValue();
            hash = zobristHash.update(hash, point.x(), point.y(), color.getValue());

            if (score >= beta) {
                transpositionTable.store(hash, beta, TranspositionEntry.LOWER_BOUND, depth, color);
                return beta;
            }
            if (score > alpha) {
                alpha = score;
                if (depth == DEFAULT_SEARCH_DEPTH) {
                    this.bestPoint = point;
                }
            }
        }

        int evaluationType = (alpha <= origAlpha) ? TranspositionEntry.UPPER_BOUND : TranspositionEntry.EXACT;
        transpositionTable.store(hash, alpha, evaluationType, depth, color);

        return alpha;
    }
}
