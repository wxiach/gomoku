package cn.wxiach.event.support;

import cn.wxiach.core.state.support.GameStateReadable;
import cn.wxiach.event.GomokuEvent;

public class BoardUpdateEvent extends GomokuEvent {

    private final GameStateReadable state;

    public BoardUpdateEvent(Object source, GameStateReadable state) {
        super(source);
        this.state = state;
    }

    public GameStateReadable getState() {
        return state;
    }
}
