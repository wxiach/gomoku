package cn.wxiach.event.types;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.gomoku.store.GomokuStore;

public class BoardUpdateEvent extends GomokuEvent {

    private final GomokuStore store;

    public BoardUpdateEvent(Object source, GomokuStore store) {
        super(source);
        this.store = store;
    }

    public GomokuStore getStore() {
        return store;
    }
}
