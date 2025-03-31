package cn.wxiach.core.state.support;

import cn.wxiach.model.Piece;

import java.util.List;

public interface BoardStateReadable extends TurnStateReadable {

    List<Piece> pieces();

    char[][] board();
}
