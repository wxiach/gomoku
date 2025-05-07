package cn.wxiach.robot.search;


import cn.wxiach.features.pattern.Patterns;
import cn.wxiach.utils.Log;
import cn.wxiach.utils.MathUtils;

import java.util.function.Function;

public class IterativeDeepeningSearch {

    public static void search(Function<Integer, Integer> searchFunction, int maxDepth) {
        for (int depth = 2; depth <= maxDepth; depth += 2) {
            Log.debug("Iterative deepening search start in {} depth.", depth);
            int value = searchFunction.apply(depth);
            if (MathUtils.approximateEqual(value, Patterns.A5.value(), 1.2)) {
                Log.info("Iterative deepening Search finish in {} depth, the value is {}", depth, value);
                break;
            }
        }
    }


}
