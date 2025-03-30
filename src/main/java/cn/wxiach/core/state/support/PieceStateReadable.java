package cn.wxiach.core.state.support;

import cn.wxiach.model.Color;

public interface PieceStateReadable {

    Color selfColor();

    Color opponentColor();
}
