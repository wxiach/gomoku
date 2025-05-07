package cn.wxiach.robot;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.types.StonePlaceEvent;
import cn.wxiach.gomoku.store.GomokuStore;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Level;
import cn.wxiach.model.Stone;
import cn.wxiach.robot.search.AlphaBetaSearch;
import cn.wxiach.robot.search.IterativeDeepeningSearch;
import cn.wxiach.robot.search.ThreatSearch;
import cn.wxiach.robot.support.TranspositionTable;
import cn.wxiach.robot.support.ZobristHash;
import cn.wxiach.utils.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class RobotEngine {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final AlphaBetaSearch alphaBetaSearch = new AlphaBetaSearch(new ZobristHash(), new TranspositionTable());

    private Level level;

    public void startCompute(GomokuStore store) {
        executorService.submit(() -> {
            try {
                AtomicReference<Stone> stone = new AtomicReference<>();

                if (store.getBoardState().stoneSequence().isEmpty()) {
                    stone.set(Stone.of(Board.SIZE / 2, Board.SIZE / 2, Color.BLACK));
                } else {
                    IterativeDeepeningSearch.search(
                            (depth) -> alphaBetaSearch.execute(
                                    store.getBoardState().board().copy(),
                                    store.getTurnState().currentTurn(),
                                    depth + ThreatSearch.THREAT_SEARCH_DEPTH,
                                    (result) -> stone.set((Stone) result)
                            ),
                            level.value()
                    );
                }

                if (stone.get() == null) {
                    throw new RobotException("Robot compute a null point.");
                } else {
                    GomokuEventBus.getInstance().publish(new StonePlaceEvent(this, stone.get()));
                }
            } catch (Exception e) {
                Log.error("Robot compute error: ", e);
            }
        });
    }

    public void updateRobotLevel(Level robotLevel) {
        this.level = robotLevel;
    }
}
