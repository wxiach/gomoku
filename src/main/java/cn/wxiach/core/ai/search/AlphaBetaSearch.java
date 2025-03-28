package cn.wxiach.core.ai.search;

import cn.wxiach.core.ai.evaluator.Evaluator;
import cn.wxiach.core.ai.evaluator.FeatureBasedEvaluator;
import cn.wxiach.core.rule.GameStateCheck;
import cn.wxiach.core.state.PieceColorState;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;

public class AlphaBetaSearch {

    public static final int ALPHA_BETA_SEARCH_DEFAULT_DEEP = 4;

    private final int depth;
    private final CandidatePointSearch candidatePointSearch;

    private final Evaluator evaluator;

    private final int[][] board;
    private final Color robotColor;

    private Point move;

    public AlphaBetaSearch(int[][] board, Color robotColor) {
        this(ALPHA_BETA_SEARCH_DEFAULT_DEEP, board, robotColor);
    }

    public AlphaBetaSearch(int depth, int[][] board, Color robotColor) {
        this.depth = depth;
        this.board = board;
        this.robotColor = robotColor;
        this.candidatePointSearch = new CandidatePointSearch(board);
        this.evaluator = new FeatureBasedEvaluator();
    }

    public Point execute() {
        alphaBeta(depth, Integer.MIN_VALUE+10000, Integer.MAX_VALUE-10000, robotColor);
        return move;
    }


    public int alphaBeta(int depth, int alpha, int beta, Color color) {
        if (depth == 0 || GameStateCheck.isGameOver(board)) {
            return evaluator.evaluate(board, color);
        }

        for (Point point : candidatePointSearch.getAllCandidatePoints()) {
            board[point.x()][point.y()] = color.getValue();
            int score = -alphaBeta(depth - 1, -beta, -alpha, PieceColorState.reverseColor(color));
            board[point.x()][point.y()] = Color.BLANK.getValue();
            if (score >= beta) return beta;
            if (score > alpha) {
                alpha = score;
                if (depth == this.depth) {
                    this.move = point;
                }
            }
        }

        return alpha;
    }
}
