package cn.wxiach.core.state.support;

import cn.wxiach.model.Color;

public interface TurnStateReadable extends PieceStateReadable {

    Color currentTurn();

    boolean isOpponentTurn();

    boolean isSelfTurn();
}
