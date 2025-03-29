package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Board;

public class BoardUpdateEvent extends GomokuEvent {

    private final Board board;

    public BoardUpdateEvent(Object source, Board board) {
        super(source);
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }
}
