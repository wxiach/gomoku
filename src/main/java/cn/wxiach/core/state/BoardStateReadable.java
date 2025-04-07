package cn.wxiach.core.state;

import cn.wxiach.model.Board;
import cn.wxiach.model.Stone;

import java.util.LinkedHashSet;

public interface BoardStateReadable extends TurnStateReadable {

    LinkedHashSet<Stone> stoneSequence();

    Board board();
}
