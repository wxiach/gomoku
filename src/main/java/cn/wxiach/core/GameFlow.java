package cn.wxiach.core;

import cn.wxiach.config.GomokuConf;
import cn.wxiach.core.ai.RobotEngine;
import cn.wxiach.core.state.GameState;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.*;
import cn.wxiach.model.Color;
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
            GomokuEventBus.getInstance().publish(new NewTurnEvent(this));
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
                GomokuEventBus.getInstance().publish(new NewTurnEvent(this));
            }
        });
    }

    private void subscribeToGameSettingsEvents() {
        GomokuEventBus.getInstance().subscribe(PieceSelectEvent.class, event -> {
            state.setSelfColor(event.getColor());
        });
    }

    public void updateGameSettings(Map<String, Object> config) {
        if (config.get(GomokuConf.SELF_PIECE_COLOR) instanceof Color selfColor) {
            state.setSelfColor(selfColor);
        }
    }
}
