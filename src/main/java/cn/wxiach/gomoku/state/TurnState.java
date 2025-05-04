package cn.wxiach.gomoku.state;

import cn.wxiach.gomoku.rule.TurnSwitcher;
import cn.wxiach.model.Color;

public class TurnState extends StoneState implements TurnSwitcher, TurnStateReadable {

    /*
     * The black stone move first by default.
     */
    private Color currentTurn = Color.BLACK;

    @Override
    public void switchTurn() {
        currentTurn = Color.reverse(currentTurn);
    }

    @Override
    public Color currentTurn() {
        return this.currentTurn;
    }

    @Override
    public boolean isOpponentTurn() {
        return currentTurn == opponentColor();
    }

    @Override
    public boolean isSelfTurn() {
        return currentTurn == selfColor();
    }

    protected void reset() {
        currentTurn = Color.BLACK;
    }

}
