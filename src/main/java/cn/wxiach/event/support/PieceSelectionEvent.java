package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Piece;

public class PieceSelectionEvent extends GomokuEvent {
    private final Piece.Color selectedPieceColor;

    public PieceSelectionEvent(Object source, Piece.Color selectedPieceColor) {
        super(source);
        this.selectedPieceColor = selectedPieceColor;
    }

    public Piece.Color getSelectedPieceColor() {
        return selectedPieceColor;
    }
}
