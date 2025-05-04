package cn.wxiach.gomoku.state;

import cn.wxiach.model.Color;

/**
 * Manage all player's stone color
 */
public abstract class StoneState implements StoneStateReadable {

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
