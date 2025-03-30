package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Piece;

public class PiecePlaceEvent extends GomokuEvent {
    private final Piece piece;

    public PiecePlaceEvent(Object source, Piece piece) {
        super(source);
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }
}
