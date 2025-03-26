package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;

public class PiecePlacedEvent extends GomokuEvent {
    public PiecePlacedEvent(Object source) {
        super(source);
    }
}
