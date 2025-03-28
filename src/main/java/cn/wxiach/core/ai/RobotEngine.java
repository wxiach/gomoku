package cn.wxiach.core.ai;

import cn.wxiach.core.ai.search.AlphaBetaSearch;
import cn.wxiach.core.state.PieceColorState;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.RobotClickEvent;
import cn.wxiach.event.support.RobotComputeEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RobotEngine {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RobotEngine(PieceColorState pieceColorState) {
        GomokuEventBus.getInstance().subscribe(RobotComputeEvent.class, event -> {
            AlphaBetaSearch alphaBetaSearch = new AlphaBetaSearch(event.getBoard(), pieceColorState.getRobotColor());
            executorService.submit(() -> {
                GomokuEventBus.getInstance().publish(new RobotClickEvent(this, alphaBetaSearch.execute()));
            });
        });
    }
}
