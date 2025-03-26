package cn.wxiach.core;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.RobotClickEvent;
import cn.wxiach.event.support.RobotComputeEvent;

public class RobotEngine {

    public RobotEngine() {
        GomokuEventBus.getInstance().subscribe(RobotComputeEvent.class, event -> {
            this.computeRobotPlacePosition(event.getBoard());
        });
    }

    private void computeRobotPlacePosition(int[][] board) {
        int[] position = new int[2];
        position[0] = 6;
        position[1] = 6;
        GomokuEventBus.getInstance().publish(new RobotClickEvent(this, position[0], position[1]));
    }
}
