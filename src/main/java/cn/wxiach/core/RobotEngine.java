package cn.wxiach.core;

import cn.wxiach.ai.BestMoveGenerator;
import cn.wxiach.config.GomokuConf;
import cn.wxiach.core.state.support.GameStateReadable;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.PiecePlaceEvent;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Difficult;
import cn.wxiach.model.Piece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RobotEngine {

    private static final Logger logger = LoggerFactory.getLogger(RobotEngine.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final BestMoveGenerator bestMoveGenerator;

    public RobotEngine() {
        this.bestMoveGenerator = new BestMoveGenerator(
                convertRobotLevelToSearchDepth(GomokuConf.defaultDifficult));
    }

    public void startCompute(GameStateReadable state) {
        executorService.submit(() -> {
            try {
                Piece piece;
                if (state.boardPieces().isEmpty()) {
                    piece = Piece.of(Board.SIZE / 2, Board.SIZE / 2, Color.BLACK);
                } else {
                    piece = bestMoveGenerator.execute(state);
                }
                if (piece == null) {
                    throw new RobotException("Robot compute a null point.");
                } else {
                    GomokuEventBus.getInstance().publish(new PiecePlaceEvent(this, piece));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateRobotLevel(Difficult robotLevel) {
        this.bestMoveGenerator.setSearchDepth(
                convertRobotLevelToSearchDepth(robotLevel));
    }

    private int convertRobotLevelToSearchDepth(Difficult robotLevel) {
        return switch (robotLevel) {
            case DIFFICULT -> 6;
            case NORMAL -> 4;
            case EASY -> 2;
        };
    }
}
