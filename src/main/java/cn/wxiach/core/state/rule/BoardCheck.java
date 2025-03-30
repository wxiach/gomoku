package cn.wxiach.core.state.rule;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.model.Point;

public interface BoardCheck {

    static boolean isOnBoard(Point point) {
        if (point.x() >= 0 && point.x() < Board.BOARD_SIZE && point.y() >= 0 && point.y() < Board.BOARD_SIZE) {
            return true;
        } else {
            throw new OutOfBoardException(String.format("The Piece [%s, %s] is out of board", point.x(), point.y()));
        }
    }

    static boolean isOnBoard(Piece piece) {
        return isOnBoard(piece.point());
    }

    static boolean containsPiece(char[][] board, Piece piece) {
        return isOnBoard(piece) && board[piece.point().x()][piece.point().y()] == piece.color().getValue();
    }

    static boolean absentPiece(char[][] board, Piece piece) {
        return !containsPiece(board, piece);
    }

    static boolean isEmpty(char[][] board, Point point) {
        return isOnBoard(point) && board[point.x()][point.y()] == Color.EMPTY.getValue();
    }

    static boolean isOccupied(char[][] board, Point point) {
        return !isEmpty(board, point);
    }
}
