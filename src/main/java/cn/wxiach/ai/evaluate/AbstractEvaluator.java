package cn.wxiach.ai.evaluate;

import cn.wxiach.ai.pattern.GomokuShapeDetector;
import cn.wxiach.ai.pattern.Pattern;
import cn.wxiach.ai.pattern.ShapeDetector;
import cn.wxiach.model.Board;


public abstract class AbstractEvaluator implements Evaluator {

    protected final ShapeDetector detector = new GomokuShapeDetector();

    /**
     * Evaluates the board's score without considering the stones color
     *
     * @param board An abstract representation of the board that does not consider stone colors.
     *              - '0' represents an empty position.
     *              - '1' represents my stone.
     *              - '2' represents a blocked position, which belongs to the opponent.
     * @return The evaluation score of the board.
     */
    @Override
    public int evaluate(Board board) {
        return detector.detect(board).stream().mapToInt(Pattern::value).sum();
    }

}
