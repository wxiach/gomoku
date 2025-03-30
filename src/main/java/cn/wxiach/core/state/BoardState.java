package cn.wxiach.core.state;

import cn.wxiach.core.state.rule.BoardCheck;
import cn.wxiach.core.state.support.BoardStateReadable;
import cn.wxiach.model.Board;
import cn.wxiach.model.Piece;

import java.util.List;

public class BoardState extends TurnState implements BoardCheck, BoardStateReadable {

    private final Board board = new Board();

    @Override
    public List<Piece> pieces() {
        return board.pieces();
    }

    @Override
    public char[][] board() {
        return board.board();
    }

    public void addPiece(Piece piece) {
        if (BoardCheck.isEmpty(board.board(), piece.point())) {
            board.addPiece(piece);
        }
    }

    @Override
    protected void reset() {
        super.reset();
        board.reset();
    }
}
