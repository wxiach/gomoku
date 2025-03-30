package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;

public class NewTurnEvent extends GomokuEvent {
    public NewTurnEvent(Object source) {
        super(source);
    }
}
