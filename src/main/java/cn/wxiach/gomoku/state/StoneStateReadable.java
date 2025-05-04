package cn.wxiach.gomoku.state;

import cn.wxiach.model.Color;

public interface StoneStateReadable {

    Color selfColor();

    Color opponentColor();
}
