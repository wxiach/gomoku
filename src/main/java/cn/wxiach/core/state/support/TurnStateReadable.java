package cn.wxiach.core.state.support;

import cn.wxiach.model.Color;

public interface TurnStateReadable {

    Color currentTurn();

    boolean isOpponentTurn();

    boolean isSelfTurn();
}
