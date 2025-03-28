package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Point;

public class HumanClickEvent extends GomokuEvent {
    private final Point point;

    public HumanClickEvent(Object source, Point point) {
        super(source);
        this.point = point;
    }

    public Point getPoint() {
        return this.point;
    }
}
