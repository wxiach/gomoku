package cn.wxiach.ui.action;

import cn.wxiach.core.model.Color;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.ui.common.components.AbstractGameButton;


public class SurrenderButton extends AbstractGameButton {

    public SurrenderButton() {
        super("认输", false);
    }

    @Override
    protected void handleButtonClick() {
        setEnabled(false);
        publish(new GameOverEvent(this, Color.EMPTY));
    }

    @Override
    protected void subscribeToEvents() {
        subscribe(GameStartEvent.class, event -> setEnabled(true));
        subscribe(GameOverEvent.class, event -> setEnabled(false));
    }
}