package cn.wxiach.core.state;

import cn.wxiach.core.rule.TurnSwitch;
import cn.wxiach.core.state.support.TurnStateReadable;
import cn.wxiach.model.Color;

public class TurnState extends PieceState implements TurnSwitch, TurnStateReadable {

    // The black piece move first by default.
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
