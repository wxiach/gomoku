package cn.wxiach.core;

import cn.wxiach.model.Piece;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.PiecePlacedEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class BoardManager {

    public static final int BOARD_SIZE = 15;

    private final LinkedHashSet<Piece> pieces = new LinkedHashSet<>();
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

    public BoardManager() {
    }

    public int[][] getBoard() {
        // Return a copy to protect the board not be modified.
        return Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
    }

    public Piece getLastPiece() {
        return pieces.isEmpty() ? null : pieces.getLast();
    }

    public void addPiece(Piece piece) {
        if (isOutOfBounds(piece.getX(), piece.getY())) {
            throw new InvalidPositionException("Piece if out of the board.");
        }
        if (hasPiece(piece.getX(), piece.getY())) {
            throw new PositionOccupiedException("There has been a piece.");
        }

        pieces.add(piece);
        convertToArray();


        GomokuEventBus.getInstance().publish(new PiecePlacedEvent(this, board));
    }

    private void convertToArray() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        pieces.forEach(piece -> {
            board[piece.getX()][piece.getY()] = piece.getColor().getValue();
        });
    }

    private boolean isOutOfBounds(int x, int y) {
        if (x < 0 || x >= BOARD_SIZE) {
            return true;
        }
        return y < 0 || y >= BOARD_SIZE;
    }

    private boolean hasPiece(int x, int y) {
        return board[x][y] == Piece.Color.WHITE.getValue() || board[x][y] == Piece.Color.BLACK.getValue();
    }

}
