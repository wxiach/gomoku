package cn.wxiach.gomoku.rule;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.utils.Log;

public class BoardCheck {

    public static boolean isOnBoard(Point point) {
        if (point.x() >= 0 && point.x() < Board.SIZE && point.y() >= 0 && point.y() < Board.SIZE) {
            return true;
        }
        Log.debug("The Stone [{}, {}] is out of board", point.x(), point.y());
        return false;
    }

    public static boolean isEmpty(Board board, Point point) {
        return isOnBoard(point) && board.stone(point).color() == Color.EMPTY;
    }

    public static boolean isOccupied(Board board, Point point) {
        return !isEmpty(board, point);
    }
}
