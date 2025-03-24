package cn.wxiach.core;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.RobotComputeEvent;

public class RobotEngine {

    public RobotEngine() {
        GomokuEventBus.getInstance().subscribe(RobotComputeEvent.class, event -> {

        });
    }
}
