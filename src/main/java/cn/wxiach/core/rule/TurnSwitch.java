package cn.wxiach.core.rule;

import cn.wxiach.model.Color;

public interface TurnSwitch {

    void switchTurn();

    Color getCurrentTurn();

    boolean isRobotTurn(Color robotColor);

    boolean isHumanTurn(Color humanColor);

}
