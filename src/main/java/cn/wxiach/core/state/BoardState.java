package cn.wxiach.core.state;

import cn.wxiach.core.rule.BoardCheck;
import cn.wxiach.core.rule.PositionOccupiedException;
import cn.wxiach.core.state.support.BoardStateReadable;
import cn.wxiach.core.utils.BoardUtils;
import cn.wxiach.model.Board;
import cn.wxiach.model.Piece;

import java.util.List;
import java.util.NoSuchElementException;

public class BoardState extends TurnState implements BoardCheck, BoardStateReadable {

    private final Board board = new Board();

    @Override
    public List<Piece> boardPieces() {
        // Return to the copy, make sure the original list cannot be modified
        return List.copyOf(board.pieces()).reversed();
    }

    @Override
    public char[][] boardMatrix() {
        return BoardUtils.deepCopy2D(board.matrix());
    }

    @Override
    public Piece lastPiece() {
        /*
         * Since pieces are stored in an ArrayDeque as a stack (LIFO),
         * the most recently placed piece is at the front (peek).
         */
        return board.last();
    }

    @Override
    public Board copyBoard() {
        return board.copy();
    }

    public void addPiece(Piece piece) {
        if (BoardCheck.isOccupied(boardMatrix(), piece.point())) {
            throw new PositionOccupiedException("");
        }
        board.add(piece);
    }

    public void revert(int count) {
        while (count > 0) {
            try {
                board.remove();
            } catch (NoSuchElementException ignore) {
            } finally {
                count--;
            }
        }
    }

    @Override
    protected void reset() {
        super.reset();
        board.reset();
    }
}
