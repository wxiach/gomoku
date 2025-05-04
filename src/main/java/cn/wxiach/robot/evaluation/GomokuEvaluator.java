package cn.wxiach.robot.evaluation;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.utils.BoardUtils;


public class GomokuEvaluator extends AbstractEvaluator {

    /**
     * Evaluates the board's score, taking the opponent's score into account.
     *
     * @param board
     * @param color Determines whether the board is evaluated from the perspective of Black or White.
     * @return
     */
    public int evaluate(Board board, Color color) {

        Board opponentBoard = BoardUtils.reverseStoneColorOnBoard(board.copy());
        // My score minus my opponent's score is the final board's score
        int value = evaluate(board) - evaluate(opponentBoard);

        /*
         * The evaluate(char[][] board) function does not recognize stone colors directly.
         * It only identifies '1' as my stone and '2' as the opponent's stone.
         * Therefore, if the current color is white, we need to invert the score.
         */
        return color == Color.WHITE ? -value : value;
    }
}
