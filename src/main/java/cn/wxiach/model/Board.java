package cn.wxiach.model;

import cn.wxiach.utils.GomokuUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    public static final int BOARD_SIZE = 15;

    private final List<Piece> pieces = new ArrayList<>();
    private final char[][] board = new char[BOARD_SIZE][BOARD_SIZE];

    public Board() {
        for (char[] chars : board) {
            Arrays.fill(chars, Color.EMPTY.getValue());
        }
    }

    public List<Piece> pieces() {
        // Return to the copy, make sure the original list cannot be modified
        return List.copyOf(pieces);
    }

    public char[][] board() {
        return GomokuUtils.deepCopy2D(board);
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
        board[piece.point().x()][piece.point().y()] = piece.color().getValue();
    }

    public void deletePiece(Piece piece) {
        pieces.remove(piece);
        board[piece.point().x()][piece.point().y()] = Color.EMPTY.getValue();
    }

    public void reset() {
        pieces.clear();
        ((ArrayList<Piece>)pieces).trimToSize();

        for (char[] chars : board) {
            Arrays.fill(chars, Color.EMPTY.getValue());
        }
    }
}
