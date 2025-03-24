package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;

public class RobotComputeEvent extends GomokuEvent {
    private final int[][] board;

    public RobotComputeEvent(Object source, int[][] board) {
        super(source);
        this.board = board;
    }

    public int[][] getBoard() {
        return board;
    }
}
