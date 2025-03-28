package cn.wxiach.core.rule;

import cn.wxiach.core.state.PieceColorState;
import cn.wxiach.model.Color;

public class RuleEngine implements TurnSwitch, GameStateCheck, PositionCheck {

    // The black piece move first by default.
    private Color currentTurn = Color.BLACK;

    @Override
    public void switchTurn() {
        currentTurn = PieceColorState.reverseColor(currentTurn);
    }

    @Override
    public Color getCurrentTurn() {
        return this.currentTurn;
    }

    @Override
    public boolean isRobotTurn(Color robotColor) {
        return this.currentTurn == robotColor;
    }

    @Override
    public boolean isHumanTurn(Color humanColor) {
        return this.currentTurn == humanColor;
    }
}
