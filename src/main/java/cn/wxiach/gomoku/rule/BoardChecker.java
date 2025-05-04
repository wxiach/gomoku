package cn.wxiach.gomoku.rule;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardChecker {

    private final static Logger logger = LoggerFactory.getLogger(BoardChecker.class);

    public static boolean isOnBoard(Point point) {
        if (point.x() >= 0 && point.x() < Board.SIZE && point.y() >= 0 && point.y() < Board.SIZE) {
            return true;
        }
        logger.debug("The Stone [{}, {}] is out of board", point.x(), point.y());
        return false;
    }

    public static boolean isEmpty(Board board, Point point) {
        return isOnBoard(point) && board.color(point) == Color.EMPTY;
    }

    public static boolean isOccupied(Board board, Point point) {
        return !isEmpty(board, point);
    }
}
