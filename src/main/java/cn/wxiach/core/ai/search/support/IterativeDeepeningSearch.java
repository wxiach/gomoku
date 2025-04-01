package cn.wxiach.core.ai.search.support;


import cn.wxiach.core.state.rule.GameStateCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class IterativeDeepeningSearch {

    private static final Logger logger = LoggerFactory.getLogger(IterativeDeepeningSearch.class);

    private final int maxDepth;
    private final int step;

    public IterativeDeepeningSearch(int maxDepth, int step) {
        this.maxDepth = maxDepth;
        this.step = step;
    }

    public int search(Function<Integer, Integer> searchFunction) {
        int score = 0;
        for (int depth = 2; depth <= maxDepth; depth += step) {
            logger.debug("Iterative deep search start in {} depth.", depth);
            score = searchFunction.apply(depth);
            if (score >= GameStateCheck.WIN_SCORE) break;
        }
        return score;
    }
}
