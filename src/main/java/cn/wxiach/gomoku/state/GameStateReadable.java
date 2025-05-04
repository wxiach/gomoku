package cn.wxiach.gomoku.state;

import cn.wxiach.model.Color;

public interface GameStateReadable extends BoardStateReadable {
    Color winner();

    boolean isOver();
}
