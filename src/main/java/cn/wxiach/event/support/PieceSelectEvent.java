package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Color;

public class PieceSelectEvent extends GomokuEvent {
    private final Color color;

    public PieceSelectEvent(Object source, Color color) {
        super(source);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
