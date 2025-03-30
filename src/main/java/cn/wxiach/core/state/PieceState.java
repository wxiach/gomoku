package cn.wxiach.core.state;

import cn.wxiach.core.state.support.PieceStateReadable;
import cn.wxiach.model.Color;

/**
 * Manage all player's piece color
 */
public abstract class PieceState implements PieceStateReadable {

    private Color selfColor;

    private Color opponentColor;

    public Color selfColor() {
        return this.selfColor;
    }

    public Color opponentColor() {
        return this.opponentColor;
    }

    public void setSelfColor(Color selfColor) {
        this.selfColor = selfColor;
        this.opponentColor = Color.reverse(selfColor);
    }
}
