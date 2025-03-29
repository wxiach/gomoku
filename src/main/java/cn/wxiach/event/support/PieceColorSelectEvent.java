package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Color;

public class PieceColorSelectEvent extends GomokuEvent {
    private final Color humanColor;

    public PieceColorSelectEvent(Object source, Color humanColor) {
        super(source);
        this.humanColor = humanColor;
    }

    public Color getHumanColor() {
        return humanColor;
    }
}
