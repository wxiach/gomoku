package cn.wxiach.ui.action;

import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.event.support.NewTurnEvent;
import cn.wxiach.event.support.RevertStoneEvent;
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
        subscribe(NewTurnEvent.class, event -> setEnabled(event.getTurn().isSelfTurn()));
    }
}
