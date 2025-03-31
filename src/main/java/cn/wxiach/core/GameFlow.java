package cn.wxiach.core;

import cn.wxiach.config.GomokuConf;
import cn.wxiach.core.ai.RobotEngine;
import cn.wxiach.core.state.GameState;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.*;
import cn.wxiach.model.Color;
import cn.wxiach.model.Difficult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class GameFlow {

    private static final Logger logger = LoggerFactory.getLogger(GameFlow.class);

    private final GameState state = new GameState();
    private final RobotEngine robot = new RobotEngine();

    public GameFlow() {
        subscribeToGameStateEvents();
        subscribeToGameSettingsEvents();
        subscribeToGameInteractionEvents();
    }

    private void subscribeToGameStateEvents() {
        GomokuEventBus.getInstance().subscribe(GameStartEvent.class, event -> {
            state.run();
            // The game state has been recharged, so the UI has also been updated
            GomokuEventBus.getInstance().publish(new BoardUpdateEvent(this, state));
            GomokuEventBus.getInstance().publish(new NewTurnEvent(this, state.currentTurn()));
        });

        GomokuEventBus.getInstance().subscribe(NewTurnEvent.class, event -> {
            if (state.isOpponentTurn()) {
                robot.startCompute(state);
            }
        });

        GomokuEventBus.getInstance().subscribe(GameOverEvent.class, event -> {
            state.end();
        });
    }

    private void subscribeToGameInteractionEvents() {
        GomokuEventBus.getInstance().subscribe(PiecePlaceEvent.class, event -> {
            if (state.isOver()) return;

            // Prevent multiple quick clicks
            if (event.getPiece().color() != state.currentTurn()) return;

            state.addPiece(event.getPiece());

            // If the code runs here, it means the piece has been placed successfully.

            GomokuEventBus.getInstance().publish(new BoardUpdateEvent(this, state));

            if (state.isOver()) {
                GomokuEventBus.getInstance().publish(new GameOverEvent(this, state.winner()));
            } else {
                state.switchTurn();
                GomokuEventBus.getInstance().publish(new NewTurnEvent(this, state.currentTurn()));
            }
        });
    }

    private void subscribeToGameSettingsEvents() {
        GomokuEventBus.getInstance().subscribe(PieceSelectEvent.class, event -> {
            state.setSelfColor(event.getColor());
        });
        GomokuEventBus.getInstance().subscribe(DifficultSelectEvent.class, event -> {
            robot.setDifficult(event.getDifficult());
        });
    }

    public void updateGameSettings(Map<String, Object> config) {
        if (config.get(GomokuConf.SELF_PIECE_COLOR) instanceof Color selfColor) {
            state.setSelfColor(selfColor);
        }
        if (config.get(GomokuConf.DIFFICULT) instanceof Difficult difficult) {
            robot.setDifficult(difficult);
        }
    }
}
