package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;

public class RobotClickEvent  extends GomokuEvent {
    private int x;
    private int y;

    public RobotClickEvent(Object source, int x, int y) {
        super(source);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
