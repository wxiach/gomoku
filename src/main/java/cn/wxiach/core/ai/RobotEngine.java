package cn.wxiach.core.ai;

import cn.wxiach.core.ai.search.AlphaBetaSearch;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.RobotMoveEvent;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RobotEngine {

    private static final Logger looger = LoggerFactory.getLogger(RobotEngine.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public void startCompute(char[][] board) {
        AlphaBetaSearch alphaBetaSearch = new AlphaBetaSearch(board, this.color);
        executorService.submit(() -> {
            Point move = alphaBetaSearch.execute();
            if (move == null) {
               throw new RobotComputeException("Robot compute a null point.");
            } else {
                GomokuEventBus.getInstance().publish(new RobotMoveEvent(this, move));
            }
        });
    }
}
