package cn.wxiach.core;

import cn.wxiach.core.state.GameState;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.*;
import cn.wxiach.robot.RobotEngine;

public class GomokuFlow {

    private final GameState state = new GameState();
    private final RobotEngine robot = new RobotEngine();

    public GomokuFlow() {
        subscribeToGameStateEvents();
        subscribeToGameSettingsEvents();
        subscribeToGameInteractionEvents();
    }

    private void subscribeToGameStateEvents() {
        GomokuEventBus.getInstance().subscribe(GameStartEvent.class, event -> {
            state.run();
            // The game state has been recharged, so the UI has also been updated
            GomokuEventBus.getInstance().publish(new BoardUpdateEvent(this, state));
            GomokuEventBus.getInstance().publish(new NewTurnEvent(this, state));
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
        GomokuEventBus.getInstance().subscribe(StonePlaceEvent.class, event -> {
            if (state.isOver()) return;

            // Prevent multiple quick clicks
            if (event.getStone().color() != state.currentTurn()) return;

            state.placeStone(event.getStone());

            // If the code runs here, it means the stone has been placed successfully.

            GomokuEventBus.getInstance().publish(new BoardUpdateEvent(this, state));

            if (state.isOver()) {
                GomokuEventBus.getInstance().publish(new GameOverEvent(this, state.winner()));
            } else {
                state.switchTurn();
                GomokuEventBus.getInstance().publish(new NewTurnEvent(this, state));
            }
        });

        GomokuEventBus.getInstance().subscribe(RevertStoneEvent.class, event -> {
            if (state.isSelfTurn()) {
                state.revertStone(2);
                GomokuEventBus.getInstance().publish(new BoardUpdateEvent(this, state));
            }
        });
    }

    private void subscribeToGameSettingsEvents() {
        GomokuEventBus.getInstance().subscribe(StoneSelectEvent.class, event -> {
            state.setSelfColor(event.getColor());
        });
        GomokuEventBus.getInstance().subscribe(LevelSelectEvent.class, event -> {
            robot.updateRobotLevel(event.getLevel());
        });
    }
}
