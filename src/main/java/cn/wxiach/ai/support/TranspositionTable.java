package cn.wxiach.ai.support;

import cn.wxiach.model.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Cache the evaluation of all board states in Gomoku.
 */
public class TranspositionTable {

    private final static Logger logger = LoggerFactory.getLogger(TranspositionTable.class);

    private final HashMap<Long, TranspositionEntry> table = new HashMap<>();

    public void store(long zobristHash, int evaluation, int evaluationType, int depth, Color color) {
        TranspositionEntry transpositionEntry = table.get(zobristHash);
        if (transpositionEntry == null || depth >= transpositionEntry.depth()) {
            table.put(zobristHash, new TranspositionEntry(evaluation, evaluationType, depth, color));
        }
    }

    public Integer find(long zobristHash, int depth, int alpha, int beta, Color color) {
        TranspositionEntry transpositionEntry = table.get(zobristHash);
        if (transpositionEntry != null && transpositionEntry.depth() >= depth) {

            int evaluation = transpositionEntry.evaluation();
            if (transpositionEntry.color() != color) {
                evaluation = -evaluation;
            }

            logger.debug(String.format("Hit the cache in TranspositionTable. The score is: %s.", evaluation));

            if (transpositionEntry.evaluationType() == TranspositionEntry.EXACT) {
                return evaluation;
            }

            if (transpositionEntry.evaluationType() == TranspositionEntry.LOWER_BOUND && evaluation >= beta) {
                return beta;
            }

            if (transpositionEntry.evaluationType() == TranspositionEntry.UPPER_BOUND && evaluation <= alpha) {
                return alpha;
            }
        }
        return null;
    }
}
