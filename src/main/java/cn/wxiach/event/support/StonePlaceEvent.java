package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Stone;

public class StonePlaceEvent extends GomokuEvent {
    private final Stone stone;

    public StonePlaceEvent(Object source, Stone stone) {
        super(source);
        this.stone = stone;
    }

    public Stone getStone() {
        return stone;
    }
}
