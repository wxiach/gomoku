package cn.wxiach.core.utils;

import cn.wxiach.model.Color;

import java.util.Arrays;

public class BoardUtils {

    public static char[][] deepCopy2D(char[][] arr) {
        return Arrays.stream(arr).map(char[]::clone).toArray(char[][]::new);
    }

    /**
     * Revert the piece colors on chessboard.
     * - black piece is changed to white piece.
     * - white piece is changed to black piece.
     *
     * @param board
     * @return
     */
    public static char[][] revertPieceColorOfBoardMatrix(char[][] board) {
        char[][] opponentBoard = BoardUtils.deepCopy2D(board);
        for (int x = 0; x < opponentBoard.length; x++) {
            for (int y = 0; y < opponentBoard[x].length; y++) {
                if (opponentBoard[x][y] == Color.EMPTY.getValue()) continue;
                if (opponentBoard[x][y] == Color.BLACK.getValue()) {
                    opponentBoard[x][y] = Color.WHITE.getValue();
                } else {
                    opponentBoard[x][y] = Color.BLACK.getValue();
                }
            }
        }
        return opponentBoard;
    }
}
