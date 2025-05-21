package cn.wxiach.core.store.state;

import cn.wxiach.model.Color;

public class TurnState {

    // 玩家的棋子颜色
    private Color humanStoneColor;

    // 默认是黑棋先行
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
