package cn.wxiach.gomoku.state;

import cn.wxiach.model.Color;

public interface TurnStateReadable extends StoneStateReadable {

    Color currentTurn();

    boolean isOpponentTurn();

    boolean isSelfTurn();
}
