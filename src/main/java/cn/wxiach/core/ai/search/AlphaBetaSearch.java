package cn.wxiach.core.ai.search;

import cn.wxiach.core.ai.evaluator.Evaluator;
import cn.wxiach.core.ai.evaluator.FeatureBasedEvaluator;
import cn.wxiach.core.state.rule.GameStateCheck;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.model.Point;

public class AlphaBetaSearch {

    public static final int DEFAULT_SEARCH_DEPTH = 4;

    private final CandidatePointSearch candidatePointSearch;

    private final Evaluator evaluator;

    private final char[][] board;
    private final Color color;

    private Point bestPoint;

    public AlphaBetaSearch(char[][] board, Color color) {
        this.board = board;
        this.color = color;
        this.candidatePointSearch = new CandidatePointSearch(board);
        this.evaluator = new FeatureBasedEvaluator();
    }

    public Piece execute() {
        alphaBeta(DEFAULT_SEARCH_DEPTH, Integer.MIN_VALUE + 10000, Integer.MAX_VALUE - 10000, color);
        return Piece.of(bestPoint, color);
    }


    public int alphaBeta(int depth, int alpha, int beta, Color color) {
        if (depth == 0 || GameStateCheck.isOver(board)) {
            return evaluator.evaluate(board, color);
        }

        for (Point point : candidatePointSearch.obtainCandidatePoints(color)) {
            board[point.x()][point.y()] = color.getValue();
            int score = -alphaBeta(depth - 1, -beta, -alpha, Color.reverse(color));
            board[point.x()][point.y()] = Color.EMPTY.getValue();
            if (score >= beta) return beta;
            if (score > alpha) {
                alpha = score;
                if (depth == DEFAULT_SEARCH_DEPTH) {
                    this.bestPoint = point;
                }
            }
        }

        return alpha;
    }
}
