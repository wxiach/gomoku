package cn.wxiach.core.rule;


import cn.wxiach.config.GomokuConf;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.model.Point;

public interface GameStateCheck {

    int WIN_CONDITION = 5;

    static boolean isGameOver(int[][] board) {
        boolean empty = false;
        for (int x = 0; x < GomokuConf.BOARD_SIZE; x++) {
            for (int y = 0; y < GomokuConf.BOARD_SIZE; y++) {
                int color = board[x][y];
                if (color == 0) {
                    empty = true;
                    continue;
                }
                if (checkWin(board, x, y, color)) return true;
            }
        }
        return !empty;
    }


    private static boolean checkWin(int[][] board, int x, int y, int color) {
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
        for (int[] dir : directions) {
            int positive = count(board, x, y, dir[0], dir[1], color);
            int negative = count(board, x, y, -dir[0], -dir[1], color);
            if (positive + negative + 1 >= WIN_CONDITION) {
                return true;
            }
        }
        return false;
    }

    private static int count(int[][] board, int x, int y, int dx, int dy, int color) {
        int cnt = 0;
        for (int i = 1; i < WIN_CONDITION; i++) {
            int nx = x + i * dx, ny = y + i * dy;
            if (PositionCheck.isOutOfBounds(Point.of(nx, ny))
                    || !PositionCheck.hasPiece(board, Piece.of(nx, ny, Color.getColor(color)))) break;
            cnt++;
        }
        return cnt;
    }

}
