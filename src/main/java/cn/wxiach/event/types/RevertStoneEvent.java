package cn.wxiach.event.types;

import cn.wxiach.event.GomokuEvent;

public class RevertStoneEvent extends GomokuEvent {
    public RevertStoneEvent(Object source) {
        super(source);
    }
}
