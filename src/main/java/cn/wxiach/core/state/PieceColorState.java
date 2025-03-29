package cn.wxiach.core.state;

import cn.wxiach.model.Color;

/**
 * Manage all player's piece color
 */
public abstract class PieceColorState {

    protected Color humanColor;

    protected Color robotColor;


    public Color getHumanColor() {
        return this.humanColor;
    }

    public Color getRobotColor() {
        return this.robotColor;
    }

    public void setColor(Color humanColor, Color robotColor) {
        this.humanColor = humanColor;
        this.robotColor = reverseColor(humanColor);
    }

    public static Color reverseColor(Color color) {
        return color == Color.BLACK ? Color.WHITE : Color.BLACK;
    }

}
