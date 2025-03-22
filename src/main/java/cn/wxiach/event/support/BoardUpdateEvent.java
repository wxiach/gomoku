package cn.wxiach.event.support;

import cn.wxiach.domain.Piece;
import cn.wxiach.event.GomokuEvent;

public class BoardUpdateEvent extends GomokuEvent {
    private final int[][] latestBord;

    public BoardUpdateEvent(Object source, int[][] latestBoard) {
        super(source);
        this.latestBord = latestBoard;
    }

    public int[][] getLatestBord() {
        return latestBord;
    }
}
