package cn.wxiach.ui.action;

import cn.wxiach.event.types.GameOverEvent;
import cn.wxiach.event.types.GameStartEvent;
import cn.wxiach.ui.common.components.AbstractGameButton;


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
