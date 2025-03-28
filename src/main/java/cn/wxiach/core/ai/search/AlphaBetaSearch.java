package cn.wxiach.core.ai.search;

import cn.wxiach.core.ai.evaluator.Evaluator;
import cn.wxiach.core.ai.evaluator.FeatureBasedEvaluator;
import cn.wxiach.core.rule.GameStateCheck;
import cn.wxiach.core.state.PieceColorState;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;

public class AlphaBetaSearch {

    public static final int ALPHA_BETA_SEARCH_DEFAULT_DEEP = 2;

    private final int depth;
    private final CandidatePointSearch candidatePointSearch;

    private final Evaluator evaluator;

    private final int[][] board;
    private final Color robotColor;

    public AlphaBetaSearch(int[][] board, Color robotColor) {
        this(ALPHA_BETA_SEARCH_DEFAULT_DEEP, board, robotColor);
    }

    public AlphaBetaSearch(int depth, int[][] board, Color robotColor) {
        this.depth = depth;
        this.board = board;
        this.robotColor = robotColor;
        this.candidatePointSearch = new CandidatePointSearch(board);
        this.evaluator = new FeatureBasedEvaluator(robotColor);
    }

    public Point execute() {
        MoveDecision move = new MoveDecision(Point.of(7, 7), Integer.MIN_VALUE);
        for (Point point : candidatePointSearch.getAllCandidatePoints()) {
            board[point.x()][point.y()] = robotColor.getValue();
            int score = alphaBeta(depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            board[point.x()][point.y()] = Color.BLANK.getValue();
            if (move.score() < score) {
                move = new MoveDecision(point, score);
            }
        }
        return move.point();
    }


    public int alphaBeta(int depth, int alpha, int beta, boolean maximizing) {
        if (depth == 0 || GameStateCheck.isGameOver(board)) {
            return evaluator.evaluate(board);
        }

        int currentScore = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Color currentColor = maximizing ? robotColor : PieceColorState.reverseColor(robotColor);

        for (Point point : candidatePointSearch.getAllCandidatePoints()) {
            board[point.x()][point.y()] = currentColor.getValue();
            int score = alphaBeta(depth - 1, alpha, beta, !maximizing);
            board[point.x()][point.y()] = Color.BLANK.getValue();
            currentScore = maximizing ? Math.max(currentScore, score) : Math.min(currentScore, score);
            if (maximizing) {
                alpha = Math.max(alpha, currentScore);
            } else {
                beta = Math.min(beta, currentScore);
            }
            if (beta <= alpha) break;
        }
        return currentScore;
    }
}
