package cn.wxiach.domain;

import java.util.Arrays;

public class Board {

    public static final int BOARD_SIZE = 15;

    private final Piece[][] board = new Piece[BOARD_SIZE][BOARD_SIZE];

    public Piece[][] getBoard() {
        // Return a copy to protect the board not be modified.
        return Arrays.stream(board).map(row -> Arrays.copyOf(row, row.length)).toArray(Piece[][]::new);
    }

    public void addPiece(Piece piece) {
        if (isOutOfBounds(piece.getX(), piece.getY())) {
            throw new InvalidPositionException("Piece if out of the board.");
        }
        if (hasPiece(piece.getX(), piece.getY())) {
            throw new PositionOccupiedException("There has been a piece.");
        }
        board[piece.getX()][piece.getY()] = piece;
    }

    private boolean isOutOfBounds(int x, int y) {
        if (x < 0 || x >= BOARD_SIZE) {
            return true;
        }
        return y < 0 || y >= BOARD_SIZE;
    }

    private boolean hasPiece(int x, int y) {
        return null != board[x][y];
    }

}
