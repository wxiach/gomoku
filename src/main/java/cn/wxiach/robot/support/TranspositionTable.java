package cn.wxiach.robot.support;

import cn.wxiach.model.Color;
import cn.wxiach.utils.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache the evaluation of all board states in Gomoku.
 */
public class TranspositionTable {

    private final Map<Long, TranspositionTable.Entry> table = new ConcurrentHashMap<>();

    public void store(long zobristHash, int evaluation, int evaluationType, int depth, Color color) {
        TranspositionTable.Entry newEntry = new Entry(evaluation, evaluationType, depth, color);

        table.compute(zobristHash, (key, existingEntry) -> {
            if (existingEntry == null || newEntry.depth() >= existingEntry.depth()) {
                return newEntry;
            } else {
                return existingEntry;
            }
        });
    }

    public Integer find(long zobristHash, int depth, int alpha, int beta, Color color) {
        TranspositionTable.Entry transpositionEntry = table.get(zobristHash);
        if (transpositionEntry != null && transpositionEntry.depth() >= depth) {

            int evaluation = transpositionEntry.evaluation();
            if (transpositionEntry.color() != color) {
                evaluation = -evaluation;
            }

            Log.debug(String.format("Hit the cache in TranspositionTable. The score is: %s.", evaluation));

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
