package cn.wxiach.ai.search;

import cn.wxiach.ai.evaluate.ShapeEvaluator;
import cn.wxiach.core.rule.WinArbiter;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;

/**
 * This search strategy only care about if the current player could win in limit steps.
 */
public class KillSearch {

    private static final int MAX_SCORE = Integer.MAX_VALUE - 10000;
    private static final int NO_THREAT = 0;

    private final KillCandidateSearch candidateSearch;

    private SearchContext context;
    private SearchResult result;

    public KillSearch(ShapeEvaluator evaluator) {
        this.candidateSearch = new KillCandidateSearch(evaluator);
    }

    public SearchResult execute(SearchContext context) {
        this.context = context;
        this.result = null;
        negativeMax(context.depth(), context.color());
        return result;
    }

    private int negativeMax(int depth, Color color) {

        if (WinArbiter.checkWin(context.board())) return -MAX_SCORE;

        if (depth == 0) return NO_THREAT;

        int bestScore = -MAX_SCORE;

        for (Piece piece : candidateSearch.obtainCandidates(context.board(), color)) {
            context.board().addPiece(piece);
            int score = -negativeMax(depth - 1, Color.reverse(color));
            context.board().removeLastPiece();

            bestScore = Math.max(bestScore, score);
            if (bestScore == MAX_SCORE) {
                if (depth == context.depth()) result = new SearchResult(piece, bestScore);
                break;
            }
        }

        return bestScore;
    }
}
