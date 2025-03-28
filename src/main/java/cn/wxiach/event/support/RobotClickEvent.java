package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Point;

public class RobotClickEvent  extends GomokuEvent {

    private final Point point;

    public RobotClickEvent(Object source, Point point) {
        super(source);
        this.point = point;
    }

    public Point getPoint() {
        return this.point;
    }

}
