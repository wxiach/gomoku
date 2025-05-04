package cn.wxiach.gomoku;

import cn.wxiach.event.EventBusAware;
import cn.wxiach.event.SubscriberPriority;
import cn.wxiach.event.types.*;
import cn.wxiach.gomoku.state.GameState;
import cn.wxiach.robot.RobotEngine;

public class GomokuFlow implements EventBusAware {

    private final GameState state = new GameState();
    private final RobotEngine robot = new RobotEngine();

    public GomokuFlow() {
        subscribeToGameStateEvents();
        subscribeToGameSettingsEvents();
        subscribeToGameInteractionEvents();
    }

    private void subscribeToGameStateEvents() {
        subscribe(GameStartEvent.class, event -> {
            state.run();
            // The game state has been recharged, so the UI has also been updated
            publish(new BoardUpdateEvent(this, state));
            publish(new NewTurnEvent(this, state));
        });

        subscribe(NewTurnEvent.class, event -> {
            if (state.isOpponentTurn()) {
                robot.startCompute(state);
            }
        });

        subscribe(GameOverEvent.class, event -> state.end());
    }

    private void subscribeToGameInteractionEvents() {
        subscribe(StonePlaceEvent.class, event -> {
            if (state.isOver()) return;

            // Prevent multiple quick clicks
            if (event.getStone().color() != state.currentTurn()) return;

            state.placeStone(event.getStone());

            // If the code runs here, it means the stone has been placed successfully.

            publish(new BoardUpdateEvent(this, state));

            if (state.isOver()) {
                publish(new GameOverEvent(this, state.winner()));
            } else {
                state.switchTurn();
                publish(new NewTurnEvent(this, state));
            }
        });

        subscribe(RevertStoneEvent.class, event -> {
            if (state.isSelfTurn()) {
                state.revertStone(2);
                publish(new BoardUpdateEvent(this, state));
            }
        });
    }

    private void subscribeToGameSettingsEvents() {
        subscribe(StoneSelectEvent.class, event -> state.setSelfColor(event.getColor()));
        subscribe(LevelSelectEvent.class, event -> robot.updateRobotLevel(event.getLevel()));
    }

    @Override
    public SubscriberPriority defaultSubscriberPriority() {
        return SubscriberPriority.LOGIC;
    }
}
