package cn.wxiach.core.state;

import cn.wxiach.model.Color;

public interface TurnStateReadable extends StoneStateReadable {

    Color currentTurn();

    boolean isOpponentTurn();

    boolean isSelfTurn();
}
