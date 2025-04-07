package cn.wxiach.ai.search;

import cn.wxiach.model.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Cache the evaluation of all board states in Gomoku.
 */
public class TranspositionTable {

    private final static Logger logger = LoggerFactory.getLogger(TranspositionTable.class);

    private final HashMap<Long, TranspositionTable.Entry> table = new HashMap<>();

    public void store(long zobristHash, int evaluation, int evaluationType, int depth, Color color) {
        TranspositionTable.Entry transpositionEntry = table.get(zobristHash);
        if (transpositionEntry == null || depth >= transpositionEntry.depth()) {
            table.put(zobristHash, new TranspositionTable.Entry(evaluation, evaluationType, depth, color));
        }
    }

    public Integer find(long zobristHash, int depth, int alpha, int beta, Color color) {
        TranspositionTable.Entry transpositionEntry = table.get(zobristHash);
        if (transpositionEntry != null && transpositionEntry.depth() >= depth) {

            int evaluation = transpositionEntry.evaluation();
            if (transpositionEntry.color() != color) {
                evaluation = -evaluation;
            }

            logger.debug(String.format("Hit the cache in TranspositionTable. The score is: %s.", evaluation));

            if (transpositionEntry.evaluationType() == TranspositionTable.Entry.EXACT) {
                return evaluation;
            }

            if (transpositionEntry.evaluationType() == TranspositionTable.Entry.BETA && evaluation >= beta) {
                return beta;
            }

            if (transpositionEntry.evaluationType() == TranspositionTable.Entry.ALPHA && evaluation <= alpha) {
                return alpha;
            }
        }
        return null;
    }

    public record Entry(int evaluation, int evaluationType, int depth, Color color) {
        public static final int EXACT = 0;
        public static final int BETA = 1;
        public static final int ALPHA = 2;
    }
}
