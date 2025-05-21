package cn.wxiach.event.types;

import cn.wxiach.core.store.GomokuStore;
import cn.wxiach.event.GomokuEvent;

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
