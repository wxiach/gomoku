package cn.wxiach.ai.search;


import cn.wxiach.core.rule.StandardWinArbiter;
import cn.wxiach.core.rule.WinArbiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class IterativeDeepeningSearch {

    private static final Logger logger = LoggerFactory.getLogger(IterativeDeepeningSearch.class);

    private final WinArbiter winArbiter = new StandardWinArbiter();
    private final int maxSearchDepth;

    public IterativeDeepeningSearch(int maxSearchDepth) {
        this.maxSearchDepth = maxSearchDepth;
    }

    public void search(Function<Integer, AlphaBetaSearch.Result> searchFunction) {
        for (int depth = 2; depth <= maxSearchDepth; depth += 2) {
            logger.debug("Iterative deep search start in {} depth.", depth);
            AlphaBetaSearch.Result result = searchFunction.apply(depth);
            if (winArbiter.win(result.board())) break;
        }
    }
}
