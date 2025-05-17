package cn.wxiach.gomoku;

import cn.wxiach.event.EventBusAware;
import cn.wxiach.event.SubscriberPriority;
import cn.wxiach.event.types.*;
import cn.wxiach.gomoku.rule.WinConditionCheck;
import cn.wxiach.gomoku.store.GomokuStore;
import cn.wxiach.robot.RobotEngine;

public class GomokuFlow implements EventBusAware {

		private final GomokuStore store = new GomokuStore();
		private final RobotEngine robot = new RobotEngine();

		public GomokuFlow() {
				subscribeToGameStateEvents();
				subscribeToGameSettingsEvents();
				subscribeToGameInteractionEvents();
		}

		private void subscribeToGameStateEvents() {
				subscribe(GameStartEvent.class, event -> {
						store.reset();
						// The game state has been recharged, so the UI has also been updated
						publish(new BoardUpdateEvent(this, store));
						publish(new NewTurnEvent(this, store.getTurnState()));
				});

				subscribe(NewTurnEvent.class, event -> {
						if (store.getTurnState().isRobotTurn()) {
								robot.compute(store);
						}
				});

				subscribe(GameOverEvent.class, event -> store.getGameState().setOver());
		}

		private void subscribeToGameInteractionEvents() {
				subscribe(StonePlaceEvent.class, event -> {
						if (store.getGameState().isOver()) return;

						// Prevent multiple quick clicks
						if (event.getStone().color() != store.getTurnState().currentTurn()) return;

						store.getBoardState().placeStone(event.getStone());

						// If the code runs here, it means the stone has been placed successfully.

						publish(new BoardUpdateEvent(this, store));

						if (WinConditionCheck.checkWin(store.getBoardState().board(), event.getStone())) {
								store.getGameState().setWinner(store.getTurnState().currentTurn());
								publish(new GameOverEvent(this, store.getGameState().getWinner()));
						} else {
								store.getTurnState().switchTurn();
								publish(new NewTurnEvent(this, store.getTurnState()));
						}
				});

				subscribe(RevertStoneEvent.class, event -> {
						if (store.getTurnState().isHumanTurn()) {
								store.getBoardState().revertStone(2);
								publish(new BoardUpdateEvent(this, store));
						}
				});
		}

		private void subscribeToGameSettingsEvents() {
				subscribe(StoneSelectEvent.class, event -> store.getTurnState().setHumanStoneColor(event.getColor()));
				subscribe(LevelSelectEvent.class, event -> store.getLevelState().setLevel(event.getLevel()));
		}

		@Override
		public SubscriberPriority defaultSubscriberPriority() {
				return SubscriberPriority.LOGIC;
		}
}
