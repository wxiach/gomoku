package cn.wxiach.core;

import cn.wxiach.ai.BestMoveGenerate;
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
    private final BestMoveGenerate bestMoveGenerate;

    public RobotEngine() {
        this.bestMoveGenerate = new BestMoveGenerate(
                convertRobotLevelToSearchDepth(GomokuConf.defaultDifficult));
    }

    public void startCompute(GameStateReadable state) {
        executorService.submit(() -> {
            try {
                Piece piece;
                if (state.boardPieces().isEmpty()) {
                    piece = Piece.of(Board.SIZE / 2, Board.SIZE / 2, Color.BLACK);
                } else {
                    piece = bestMoveGenerate.execute(state);
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
        this.bestMoveGenerate.setSearchDepth(
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
