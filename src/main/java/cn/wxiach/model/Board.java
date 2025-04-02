package cn.wxiach.model;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;

public class Board {

    public static final int SIZE = 15;
    ArrayDeque<Piece> pieces = new ArrayDeque<>();
    private final char[][] board = new char[SIZE][SIZE];

    public Board() {
        for (char[] chars : board) {
            Arrays.fill(chars, Color.EMPTY.getValue());
        }
    }

    public Collection<Piece> pieces() {
        return pieces;
    }

    public char[][] matrix() {
        return board;
    }

    public void add(Piece piece) {
        pieces.push(piece);
        board[piece.point().x()][piece.point().y()] = piece.color().getValue();
    }

    public void remove() {
        Piece piece = pieces.pop();
        board[piece.point().x()][piece.point().y()] = Color.EMPTY.getValue();
    }

    public Piece last() {
        return pieces.peek();
    }

    public Board copy() {
        Board copy = new Board();
        for (Piece piece : pieces) {
            copy.add(piece);
        }
        return copy;
    }

    public void reset() {
        pieces.clear();
        for (char[] chars : board) {
            Arrays.fill(chars, Color.EMPTY.getValue());
        }
    }

}
