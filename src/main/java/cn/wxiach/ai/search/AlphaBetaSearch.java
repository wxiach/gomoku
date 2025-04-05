package cn.wxiach.ai.search;

import cn.wxiach.ai.evaluate.FeatureEvaluator;
import cn.wxiach.ai.pattern.PatternCollection;
import cn.wxiach.ai.support.TranspositionEntry;
import cn.wxiach.ai.support.TranspositionTable;
import cn.wxiach.ai.support.ZobristHash;
import cn.wxiach.core.rule.WinArbiter;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;

public class AlphaBetaSearch {

    private final ZobristHash zobristHash;
    private final TranspositionTable transpositionTable;

    private final FeatureEvaluator evaluator;
    private final ABCandidateSearch candidateSearch;

    private SearchContext context;
    private SearchResult result;

    public AlphaBetaSearch(ZobristHash zobristHash, TranspositionTable transpositionTable, FeatureEvaluator evaluator) {
        this.zobristHash = zobristHash;
        this.transpositionTable = transpositionTable;
        this.evaluator = evaluator;
        this.candidateSearch = new ABCandidateSearch(evaluator);
    }

    public SearchResult execute(SearchContext context) {
        this.context = context;
        /*
         * Negamax search inverts alpha and beta during recursion.
         * To prevent integer overflow:
         * - alpha is offset by +10,000 from Integer.MIN_VALUE.
         * - beta is offset by -10,000 from Integer.MAX_VALUE.
         */
        alphaBeta(context.depth(), Integer.MIN_VALUE + 10000, Integer.MAX_VALUE - 10000, context.color());
        return result;
    }


    /**
     * @param depth
     * @param alpha
     * @param beta
     * @param color
     * @return
     */
    public int alphaBeta(int depth, int alpha, int beta, Color color) {
        long hash = zobristHash.compute(context.board());

        Integer evaluation = transpositionTable.find(hash, depth, alpha, beta, color);
        if (evaluation != null && depth != context.depth()) {
            return evaluation;
        }

        if (depth == 0) {
            if (WinArbiter.checkOver(context.board())) {
                return PatternCollection.Five.score();
            }
            return evaluator.evaluate(context.board(), color);
        }

        int origAlpha = alpha;

        for (Piece piece : candidateSearch.obtainCandidates(context.board(), color)) {

            context.board().addPiece(piece);
            hash = zobristHash.update(hash, piece);

            int score = -alphaBeta(depth - 1, -beta, -alpha, Color.reverse(color));

            /*
             * Firstly, I use a stack to manage the pieces on the board,
             * and since the recursive operations are serial,
             * there's no need to specify which piece to remove here.
             */
            context.board().removeLastPiece();

            hash = zobristHash.update(hash, piece);

            if (score >= beta) {
                transpositionTable.store(hash, beta, TranspositionEntry.LOWER_BOUND, depth, color);
                return beta;
            }
            if (score > alpha) {
                alpha = score;
                if (depth == context.depth()) {
                    result = new SearchResult(piece, score);
                }
            }
        }

        int evaluationType = (alpha <= origAlpha) ? TranspositionEntry.UPPER_BOUND : TranspositionEntry.EXACT;
        transpositionTable.store(hash, alpha, evaluationType, depth, color);

        return alpha;
    }

}
