package cn.wxiach.event.types;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.gomoku.state.GameStateReadable;

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
