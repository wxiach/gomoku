package cn.wxiach.core;

import cn.wxiach.model.Piece;

public class TurnHandler {

    // The black pieces move first by default.
    private Piece.Color currentTurn = Piece.Color.BLACK;

    private Piece.Color humanPieceColor;

    public void setHumanPieceColor(Piece.Color color) {
        this.humanPieceColor = color;
    }

    public boolean isHumanTurn() {
        return humanPieceColor == currentTurn;
    }

    public boolean isRobotTurn() {
        return humanPieceColor != currentTurn;
    }

    public Piece.Color getCurrentTurn() {
        return currentTurn;
    }

    public void switchTurn() {
       currentTurn = currentTurn == Piece.Color.WHITE ? Piece.Color.BLACK : Piece.Color.WHITE;
    }

    public void resetTurn() {
        currentTurn = Piece.Color.BLACK;
    }
}
