package cn.wxiach.core.state;

import cn.wxiach.core.model.Color;

public interface GameStateReadable extends BoardStateReadable {
    Color winner();

    boolean isOver();
}
