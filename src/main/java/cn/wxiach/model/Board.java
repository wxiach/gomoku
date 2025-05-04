package cn.wxiach.model;

import java.util.Arrays;

/**
 * Represents a 15x15 Gomoku game board.
 * <p>
 * The board uses a one-dimensional char array to store the game state for optimal performance.
 * Each position can be empty or occupied by a black/white stone.
 * The coordinate system uses the top-left corner as the origin (0,0).
 */
public class Board {

    public static final int SIZE = 15;
    private final char[] board;

    /**
     * Creates an empty game board with all positions initialized to Color.EMPTY.
     */
    public Board() {
        this.board = new char[SIZE * SIZE];
        Arrays.fill(board, Color.EMPTY.value());
    }

    /**
     * Creates a board with a predefined state.
     *
     * @param board the initial board state as a char array of length SIZE*SIZE
     * @throws IllegalArgumentException if the board array has incorrect size
     */
    public Board(char[] board) {
        if (board.length != SIZE * SIZE) {
            throw new IllegalArgumentException("Invalid board size");
        }
        this.board = board;
    }

    // Basic getters and board manipulation methods
    public char[] vector() {
        return board;
    }

    public int length() {
        return board.length;
    }

    public char get(int index) {
        return board[index];
    }

    public void set(int index, char value) {
        board[index] = value;
    }

    /**
     * Places a stone on the board. This is the primary method for making moves.
     */
    public void makeMove(Stone stone) {
        board[index(stone.point())] = stone.color().value();
    }

    /**
     * Removes a stone from the board, restoring the position to empty state.
     * Used for move undo operations.
     */
    public void undoMove(Stone stone) {
        board[index(stone.point())] = Color.EMPTY.value();
    }

    // Color and stone access methods
    public Color color(int index) {
        return Color.of(get(index));
    }

    public Color color(Point point) {
        return color(index(point));
    }

    public Stone stone(int index) {
        return Stone.of(point(index), color(index));
    }

    public Stone stone(Point point) {
        return stone(index(point));
    }

    public Point point(int index) {
        return Point.of(x(index), y(index));
    }

    public void reset() {
        Arrays.fill(board, Color.EMPTY.value());
    }

    /**
     * Converts 2D board coordinates to 1D array index.
     * <p>
     * The conversion follows the formula: index = y * SIZE + x
     * This mapping ensures efficient storage and access of board positions.
     *
     * @param x column index (0 to SIZE-1)
     * @param y row index (0 to SIZE-1)
     * @return the corresponding index in the internal array
     */
    public static int index(int x, int y) {
        return y * SIZE + x;
    }

    public static int index(Point point) {
        return index(point.x(), point.y());
    }

    /**
     * Extracts the x-coordinate (column) from an array index.
     */
    public static int x(int index) {
        return index % SIZE;
    }

    /**
     * Extracts the y-coordinate (row) from an array index.
     */
    public static int y(int index) {
        return index / SIZE;
    }

    /**
     * Creates a deep copy of the current board state.
     * Uses System.arraycopy for optimal performance.
     */
    public Board copy() {
        char[] newBoard = new char[board.length];
        System.arraycopy(board, 0, newBoard, 0, board.length);
        return new Board(newBoard);
    }
}


