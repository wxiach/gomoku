package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;

public class GameRestartEvent extends GomokuEvent {
    public GameRestartEvent(Object source) {
        super(source);
    }
}
