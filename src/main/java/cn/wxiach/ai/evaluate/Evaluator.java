package cn.wxiach.ai.evaluate;


import cn.wxiach.core.utils.BoardUtils;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;

public interface Evaluator {

    /**
     * Evaluates the board's score, taking the opponent's score into account.
     *
     * @param board
     * @param color Determines whether the board is evaluated from the perspective of Black or White.
     * @return
     */
    default int evaluate(Board board, Color color) {
        /*
         * Invert the piece colors on the chessboard to simplify the opponent's score calculation.
         * By doing this, when evaluating the opponent's board, their pieces will appear as my own.
         *
         * As a result, we can call evaluateForColor(opponentBoard, color)
         * instead of evaluateForColor(opponentBoard, opponentColor).
         */
        char[][] opponentBoard = BoardUtils.revertPieceColorOfBoardMatrix(board.matrix());

        // My score minus my opponent's score is the final board's score
        int score = evaluate(board.matrix()) - evaluate(opponentBoard);

        /*
         * The evaluate(char[][] board) function does not recognize piece colors directly.
         * It only identifies '1' as my piece and '2' as the opponent's piece.
         * Therefore, if the current color is white, we need to invert the score.
         */
        return color == Color.WHITE ? -score : score;
    }


    /**
     * Evaluates the board's score without considering the opponent's score.
     *
     * @param board An abstract representation of the board that does not consider piece colors.
     *              - '0' represents an empty position.
     *              - '1' represents my piece.
     *              - '2' represents a blocked position, which belongs to the opponent.
     * @return The evaluation score of the board.
     */
    int evaluate(char[][] board);


}
