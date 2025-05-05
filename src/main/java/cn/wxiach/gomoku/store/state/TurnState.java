package cn.wxiach.gomoku.store.state;

import cn.wxiach.model.Color;

public class TurnState {

    // The stone color of the human player
    private Color humanStoneColor;

    // Set the default turn to black
    private Color currentTurn = Color.BLACK;

    public void switchTurn() {
        currentTurn = Color.reverse(currentTurn);
    }

    public Color currentTurn() {
        return this.currentTurn;
    }

    public Color getHumanStoneColor() {
        return humanStoneColor;
    }

    public void setHumanStoneColor(Color humanStoneColor) {
        this.humanStoneColor = humanStoneColor;
    }

    public boolean isHumanTurn() {
        return currentTurn == humanStoneColor;
    }

    public boolean isRobotTurn() {
        return currentTurn != humanStoneColor;
    }

    public void reset() {
        this.currentTurn = Color.BLACK;
    }
}
