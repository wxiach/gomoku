package cn.wxiach.ui.action;

import cn.wxiach.event.types.GameOverEvent;
import cn.wxiach.event.types.GameStartEvent;
import cn.wxiach.event.types.NewTurnEvent;
import cn.wxiach.event.types.RevertStoneEvent;
import cn.wxiach.ui.common.components.AbstractGameButton;


public class RevertButton extends AbstractGameButton {

    public RevertButton() {
        super("悔棋", false);
    }

    @Override
    protected void handleButtonClick() {
        publish(new RevertStoneEvent(this));
    }

    @Override
    protected void subscribeToEvents() {
        subscribe(GameStartEvent.class, event -> setEnabled(true));
        subscribe(GameOverEvent.class, event -> setEnabled(false));
        subscribe(NewTurnEvent.class, event -> setEnabled(event.getTurn().isHumanTurn()));
    }
}
