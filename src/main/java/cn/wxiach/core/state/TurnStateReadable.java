package cn.wxiach.core.state;

import cn.wxiach.core.model.Color;

public interface TurnStateReadable extends StoneStateReadable {

    Color currentTurn();

    boolean isOpponentTurn();

    boolean isSelfTurn();
}
