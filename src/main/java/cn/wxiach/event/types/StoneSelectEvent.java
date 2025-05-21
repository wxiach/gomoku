package cn.wxiach.event.types;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Color;

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
