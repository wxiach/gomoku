package cn.wxiach.core.state;

import cn.wxiach.model.Color;

public abstract class PieceColorState {

    protected Color humanColor;

    protected Color robotColor;


    public Color getHumanColor() {
        return this.humanColor;
    }

    public Color getRobotColor() {
        return this.robotColor;
    }

    public static Color reverseColor(Color color) {
        return color == Color.BLACK ? Color.WHITE : Color.BLACK;
    }

}
