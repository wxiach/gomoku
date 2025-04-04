package cn.wxiach.ai.evaluate;

import cn.wxiach.ai.pattern.Pattern;
import cn.wxiach.ai.pattern.PatternCollection;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;

import java.util.Collection;

public class GomokuEvaluator extends AbstractEvaluator implements FeatureEvaluator, ShapeEvaluator {


    /**
     * Evaluates the board's score, taking the opponent's score into account.
     *
     * @param board
     * @param color Determines whether the board is evaluated from the perspective of Black or White.
     * @return
     */
    @Override
    public int evaluate(Board board, Color color) {

        char[][] opponentBoard = revertPieceColorOnBoard(board.matrix());

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
     * Find all patterns for the point.
     *
     * @param board
     * @param point
     * @return
     */
    @Override
    public Collection<Pattern> evaluate(Board board, Point point) {
        char[][] boardMatrix = board.matrix();
        if (Color.getColor(board.matrix()[point.x()][point.y()]) == Color.WHITE) {
            boardMatrix = revertPieceColorOnBoard(board.matrix());
        }

        Collection<Pattern> patterns = this.detector.detect(boardMatrix, point.x(), point.y());

        // Filter not important shape
        return patterns.stream().filter(pattern -> !PatternCollection.A1.equals(pattern.name())
                && !PatternCollection.A2.equals(pattern.name())).toList();
    }
}
