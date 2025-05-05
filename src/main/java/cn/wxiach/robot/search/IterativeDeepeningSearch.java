package cn.wxiach.robot.search;


import cn.wxiach.features.pattern.PatternCollection;
import cn.wxiach.utils.Log;

import java.util.function.Function;

public class IterativeDeepeningSearch {

    public static void search(Function<Integer, Integer> searchFunction, int maxDepth) {
        for (int depth = 2; depth <= maxDepth; depth += 2) {
            Log.debug("Iterative deepening search start in {} depth.", depth);
            int value = searchFunction.apply(depth);
            if (approximateEqual(value, PatternCollection.A5_VALUE, 1.2)) {
                Log.info("Iterative deepening Search finish in {} depth, the value is {}", depth, value);
                break;
            }
        }
    }

    /**
     * Determines if two double values are approximately equal within a given threshold.
     *
     * @param a         The first double value.
     * @param b         The second double value.
     * @param threshold The threshold for approximation. Must be greater than 0.
     * @return True if the values are approximately equal, false otherwise.
     */
    public static boolean approximateEqual(double a, double b, double threshold) {
        if (b == 0) {
            b = 0.01;
        }
        if (b >= 0) {
            return (a >= b / threshold) && (a <= b * threshold);
        } else {
            return (a >= b * threshold) && (a <= b / threshold);
        }
    }
}
