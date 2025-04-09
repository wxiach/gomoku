package cn.wxiach.core.utils;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;

public class BoardUtils {

    /**
     * Reverts the stone colors on the chessboard.
     * - Black stones become white.
     * - White stones become black.
     *
     * @param board
     * @return
     */
    public static Board reverseStoneColorOnBoard(Board board) {
        for (int i = 0; i < board.length(); i++) {
            char newValue = switch (board.get(i)) {
                case '1' -> Color.WHITE.value();
                case '2' -> Color.BLACK.value();
                default -> board.get(i);
            };
            board.set(i, newValue);
        }
        return board;
    }


    public static void print(Board board) {
        for (int i = 0; i < board.length(); i++) {
            char c = board.get(i) == Color.EMPTY.value() ? ' ' : board.get(i) == Color.BLACK.value() ? 'o' : '+';
            System.out.printf("%s ", c);
            if (i % 15 == 0) System.out.println();
        }
    }

    public static int countStones(Board board) {
        int cnt = 0;
        for (int i = 0; i < board.length(); i++) {
            if (board.get(i) != Color.EMPTY.value()) cnt++;
        }
        return cnt;
    }
}
