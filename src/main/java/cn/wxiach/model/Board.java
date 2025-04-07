package cn.wxiach.model;

import java.util.Arrays;

/**
 * Board represents a 15x15 game board.
 * <p>
 * The board is stored as a one-dimensional array for better performance.
 * Provides basic operations such as making a move, undoing a move, and resetting the board.
 */
public class Board {

    public static final int SIZE = 15;
    private final char[] board;

    /**
     * Initializes an empty board.
     */
    public Board() {
        this.board = new char[SIZE * SIZE];
        Arrays.fill(board, Color.EMPTY.value());
    }

    /**
     * Initializes the board with the given state.
     *
     * @param board the initial board state
     */
    public Board(char[] board) {
        if (board.length != SIZE * SIZE) {
            throw new IllegalArgumentException("Invalid board size");
        }
        this.board = board;
    }

    public char[] vector() {
        return board;
    }

    /**
     * Places a stone on the board.
     */
    public void makeMove(Stone stone) {
        board[index(stone.point())] = stone.color().value();
    }

    /**
     * Removes a stone from the board.
     */
    public void undoMove(Stone stone) {
        board[index(stone.point())] = Color.EMPTY.value();
    }

    public int length() {
        return board.length;
    }

    public char get(int index) {
        return board[index];
    }

    public Color color(int index) {
        return Color.of(get(index));
    }

    public Color color(Point point) {
        return color(index(point));
    }

    public Point point(int index) {
        return Point.of(x(index), y(index));
    }

    public Stone stone(int index) {
        return Stone.of(point(index), color(index));
    }

    public Stone stone(Point point) {
        return stone(index(point));
    }

    public void set(int index, char value) {
        board[index] = value;
    }

    /**
     * Resets the board to the initial state.
     */
    public void reset() {
        Arrays.fill(board, Color.EMPTY.value());
    }

    /**
     * Converts 2D coordinates to the corresponding 1D array index.
     * <p>
     * The coordinate system uses the top-left corner as the origin (0, 0),
     * where x represents the column and y represents the row.
     *
     * @param x the column index (horizontal axis)
     * @param y the row index (vertical axis)
     * @return the calculated index in the one-dimensional array
     */
    public static int index(int x, int y) {
        return y * SIZE + x;
    }

    public static int index(Point point) {
        return index(point.x(), point.y());
    }

    public static int x(int index) {
        return index % SIZE;
    }

    public static int y(int index) {
        return index / SIZE;
    }

    public Board copy() {
        char[] newBoard = new char[board.length];
        /*
         * System.arraycopy is JVM's native method,
         * almost equivalent to memory block copy, offering the highest efficiency.
         */
        System.arraycopy(board, 0, newBoard, 0, board.length);
        return new Board(newBoard);
    }
}


