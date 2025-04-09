package cn.wxiach.core.state;

import cn.wxiach.core.model.Board;
import cn.wxiach.core.model.Stone;

import java.util.LinkedHashSet;

public interface BoardStateReadable extends TurnStateReadable {

    LinkedHashSet<Stone> stoneSequence();

    Board board();
}
