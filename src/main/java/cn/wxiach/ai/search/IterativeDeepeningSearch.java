package cn.wxiach.ai.search;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class IterativeDeepeningSearch {

    private static final Logger logger = LoggerFactory.getLogger(IterativeDeepeningSearch.class);

    public static void search(Function<Integer, Integer> searchFunction, Function<Integer, Boolean> exitCondition, int depth, int step) {
        for (int curDepth = step; curDepth <= depth; curDepth += step) {
            logger.debug("Iterative deepening search start in {} depth.", curDepth);
            if (exitCondition.apply(searchFunction.apply(curDepth))) {
                logger.info("Iterative deepening Search finish in {} depth,", curDepth);
                break;
            }
        }
    }
}
