package cn.wxiach.ui.action;

import cn.wxiach.event.types.GameOverEvent;
import cn.wxiach.event.types.GameStartEvent;
import cn.wxiach.model.Color;
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