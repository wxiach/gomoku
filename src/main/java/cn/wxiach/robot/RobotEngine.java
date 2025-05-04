package cn.wxiach.robot;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.StonePlaceEvent;
import cn.wxiach.gomoku.state.GameStateReadable;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Level;
import cn.wxiach.model.Stone;
import cn.wxiach.robot.features.PatternCollection;
import cn.wxiach.robot.search.AlphaBetaSearch;
import cn.wxiach.robot.search.IterativeDeepeningSearch;
import cn.wxiach.robot.support.TranspositionTable;
import cn.wxiach.robot.support.ZobristHash;
import cn.wxiach.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class RobotEngine {

    private static final Logger logger = LoggerFactory.getLogger(RobotEngine.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final TranspositionTable transpositionTable = new TranspositionTable();
    private final ZobristHash zobristHash = new ZobristHash();
    private final AlphaBetaSearch alphaBetaSearch = new AlphaBetaSearch(zobristHash, transpositionTable);

    private Level robotLevel;

    public void startCompute(GameStateReadable state) {
        executorService.submit(() -> {
            try {
                AtomicReference<Stone> stone = new AtomicReference<>();
                if (state.stoneSequence().isEmpty()) {
                    stone.set(Stone.of(Board.SIZE / 2, Board.SIZE / 2, Color.BLACK));
                } else {
                    IterativeDeepeningSearch.search(
                            (depth) -> alphaBetaSearch.execute(
                                    state.board().copy(),
                                    state.currentTurn(),
                                    depth,
                                    (result) -> stone.set((Stone) result)
                            ),
                            (value) -> MathUtils.approximateEqual(value, PatternCollection.A5_VALUE, 1.2),
                            robotLevel.value(),
                            2
                    );
                }
                if (stone.get() == null) {
                    throw new RobotException("Robot compute a null point.");
                } else {
                    GomokuEventBus.getInstance().publish(new StonePlaceEvent(this, stone.get()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateRobotLevel(Level robotLevel) {
        this.robotLevel = robotLevel;
    }
}
