package cn.wxiach.core.state.rule;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.model.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface BoardCheck {

    Logger logger = LoggerFactory.getLogger(BoardCheck.class);

    static boolean isOnBoard(Point point) {
        if (point.x() >= 0 && point.x() < Board.SIZE && point.y() >= 0 && point.y() < Board.SIZE) {
            return true;
        }
        logger.debug("The Piece [{}, {}] is out of board", point.x(), point.y());
        return false;
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
