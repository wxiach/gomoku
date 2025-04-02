package cn.wxiach.core.state.support;

import cn.wxiach.model.Board;
import cn.wxiach.model.Piece;

import java.util.List;

public interface BoardStateReadable extends TurnStateReadable {

    List<Piece> boardPieces();

    char[][] boardMatrix();

    Board copyBoard();

    Piece lastPiece();
}
