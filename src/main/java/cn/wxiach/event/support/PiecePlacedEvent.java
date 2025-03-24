package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;

public class PiecePlacedEvent extends GomokuEvent {
    private final int[][] latestBord;

    public PiecePlacedEvent(Object source, int[][] latestBoard) {
        super(source);
        this.latestBord = latestBoard;
    }

    public int[][] getLatestBord() {
        return latestBord;
    }
}
