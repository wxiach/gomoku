package cn.wxiach.ui.components.buttons;

import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.ui.components.AbstractGameButton;


public class StartGameButton extends AbstractGameButton {

    public StartGameButton() {
        super("开始游戏");
    }

    @Override
    protected void handleButtonClick() {
        setEnabled(false);
        publish(new GameStartEvent(this));
    }

    @Override
    protected void subscribeToEvents() {
        subscribe(GameOverEvent.class, event -> setEnabled(true));
    }
}
