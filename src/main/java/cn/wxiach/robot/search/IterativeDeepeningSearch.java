package cn.wxiach.robot.search;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class IterativeDeepeningSearch {

    private static final Logger logger = LoggerFactory.getLogger(IterativeDeepeningSearch.class);

    public static void search(Function<Integer, Integer> searchFunction, Function<Integer, Boolean> exitCondition, int maxDepth, int step) {
        for (int depth = step; depth <= maxDepth; depth += step) {
            logger.debug("Iterative deepening search start in {} depth.", depth);
            int value = searchFunction.apply(depth);
            if (exitCondition.apply(value)) {
                logger.info("Iterative deepening Search finish in {} depth, the value is {}", depth, value);
                break;
            }
        }
    }
}
