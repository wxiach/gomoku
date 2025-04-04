package cn.wxiach.ai.evaluate;

import cn.wxiach.ai.pattern.GomokuShapeDetector;
import cn.wxiach.ai.pattern.Pattern;
import cn.wxiach.ai.pattern.ShapeDetector;
import cn.wxiach.core.utils.BoardUtils;
import cn.wxiach.model.Color;


public abstract class AbstractEvaluator implements Evaluator {

    protected final ShapeDetector detector = new GomokuShapeDetector();

    /**
     * Evaluates the board's score without considering the pieces color
     *
     * @param board An abstract representation of the board that does not consider piece colors.
     *              - '0' represents an empty position.
     *              - '1' represents my piece.
     *              - '2' represents a blocked position, which belongs to the opponent.
     * @return The evaluation score of the board.
     */
    @Override
    public int evaluate(char[][] board) {
        return detector.detect(board).stream().mapToInt(Pattern::score).sum();
    }


    /**
     * Reverts the piece colors on the chessboard.
     * - Black pieces become white.
     * - White pieces become black.
     *
     * @param board
     * @return
     */
    protected char[][] revertPieceColorOnBoard(char[][] board) {
        char[][] boardMatrix = BoardUtils.deepCopy2D(board);
        for (int x = 0; x < boardMatrix.length; x++) {
            for (int y = 0; y < boardMatrix[x].length; y++) {
                boardMatrix[x][y] = switch (boardMatrix[x][y]) {
                    case '1' -> Color.WHITE.getValue();
                    case '2' -> Color.BLACK.getValue();
                    default -> boardMatrix[x][y];
                };
            }
        }
        return boardMatrix;
    }
}
