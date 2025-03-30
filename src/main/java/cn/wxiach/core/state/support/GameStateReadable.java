package cn.wxiach.core.state.support;

import cn.wxiach.model.Color;

public interface GameStateReadable extends BoardStateReadable {
    Color winner();

    boolean isOver();
}
