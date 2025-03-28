package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Color;

public class PieceSelectionEvent extends GomokuEvent {
    private final Color selectedPieceColor;

    public PieceSelectionEvent(Object source, Color selectedPieceColor) {
        super(source);
        this.selectedPieceColor = selectedPieceColor;
    }

    public Color getSelectedPieceColor() {
        return selectedPieceColor;
    }
}
