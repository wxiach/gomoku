package cn.wxiach.event.support;

import cn.wxiach.domain.Piece;
import cn.wxiach.event.GomokuEvent;

public class PiecePlacedEvent extends GomokuEvent {
    private final Piece piece;

    public PiecePlacedEvent(Object source, Piece piece) {
        super(source);
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }
}
