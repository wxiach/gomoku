package cn.wxiach.core.rule;

import cn.wxiach.config.GomokuConf;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.model.Point;

public interface PositionCheck {

    static boolean isOutOfBounds(int x, int y) {
        if (x < 0 || x >= GomokuConf.BOARD_SIZE) {
            return true;
        }
        return y < 0 || y >= GomokuConf.BOARD_SIZE;
    }

    static boolean hasPiece(int[][] board, Piece piece) {
        return board[piece.point().x()][piece.point().y()] == piece.color().getValue();
    }

    static boolean isEmpty(int[][] board, Point point) {
        return board[point.x()][point.y()] == Color.BLANK.getValue();
    }
}
