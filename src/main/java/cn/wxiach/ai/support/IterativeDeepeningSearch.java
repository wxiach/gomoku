package cn.wxiach.ai.support;


import cn.wxiach.ai.pattern.PatternCollection;
import cn.wxiach.ai.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class IterativeDeepeningSearch {

    private static final Logger logger = LoggerFactory.getLogger(IterativeDeepeningSearch.class);

    private final int maxSearchDepth;

    public IterativeDeepeningSearch(int maxSearchDepth) {
        this.maxSearchDepth = maxSearchDepth;
    }

    public void search(Function<Integer, SearchResult> searchFunction) {
        for (int depth = 2; depth <= maxSearchDepth; depth += 2) {
            logger.debug("Iterative deep search start in {} depth.", depth);
            SearchResult searchResult = searchFunction.apply(depth);
            if (searchResult.score() >= (PatternCollection.Five.score() - (PatternCollection.aliveFour.score() * 2))) {
                logger.info("Search finish in {} depth,", depth);
                break;
            }
        }
    }
}
