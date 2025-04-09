package cn.wxiach.event.support;

import cn.wxiach.core.model.Color;
import cn.wxiach.event.GomokuEvent;

public class StoneSelectEvent extends GomokuEvent {
    private final Color color;

    public StoneSelectEvent(Object source, Color color) {
        super(source);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
