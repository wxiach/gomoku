package cn.wxiach.core.rule;


import cn.wxiach.model.Board;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;

public class WinConditionCheck {

    /**
     * 检查当前棋盘上是否有五子连珠
     *
     * @param board 棋盘
     * @param stone 最新落子
     * @return true 如果有五子连珠，false 否则
     */
    public static boolean checkWin(Board board, Stone stone) {
        int x = stone.point().x();
        int y = stone.point().y();
        char color = stone.color().value();

        // 检查水平方向
        if (checkHorizontal(board, x, y, color)) {
            return true;
        }

        // 检查垂直方向
        if (checkVertical(board, x, y, color)) {
            return true;
        }

        // 检查左上到右下对角线
        if (checkLeftDiagonal(board, x, y, color)) {
            return true;
        }

        // 检查右上到左下对角线
        return checkRightDiagonal(board, x, y, color);
    }

    private static boolean checkHorizontal(Board board, int x, int y, char color) {
        int count = 1;
        // 向左检查
        for (int i = x - 1; i >= 0 && getStoneColor(board, i, y) == color; i--) {
            count++;
        }
        // 向右检查
        for (int i = x + 1; i < Board.SIZE && getStoneColor(board, i, y) == color; i++) {
            count++;
        }
        return count >= 5;
    }

    private static boolean checkVertical(Board board, int x, int y, char color) {
        int count = 1;
        // 向上检查
        for (int i = y - 1; i >= 0 && getStoneColor(board, x, i) == color; i--) {
            count++;
        }
        // 向下检查
        for (int i = y + 1; i < Board.SIZE && getStoneColor(board, x, i) == color; i++) {
            count++;
        }
        return count >= 5;
    }

    private static boolean checkLeftDiagonal(Board board, int x, int y, char color) {
        int count = 1;
        // 向左上检查
        for (int i = x - 1, j = y - 1; i >= 0 && j >= 0 && getStoneColor(board, i, j) == color; i--, j--) {
            count++;
        }
        // 向右下检查
        for (int i = x + 1, j = y + 1; i < Board.SIZE && j < Board.SIZE && getStoneColor(board, i, j) == color; i++, j++) {
            count++;
        }
        return count >= 5;
    }

    private static boolean checkRightDiagonal(Board board, int x, int y, char color) {
        int count = 1;
        // 向右上检查
        for (int i = x + 1, j = y - 1; i < Board.SIZE && j >= 0 && getStoneColor(board, i, j) == color; i++, j--) {
            count++;
        }
        // 向左下检查
        for (int i = x - 1, j = y + 1; i >= 0 && j < Board.SIZE && getStoneColor(board, i, j) == color; i--, j++) {
            count++;
        }
        return count >= 5;
    }

    private static char getStoneColor(Board board, int x, int y) {
        return board.get(Board.index(Point.of(x, y)));
    }
}