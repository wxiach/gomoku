package cn.wxiach.robot.evaluate;

import cn.wxiach.core.model.Board;
import cn.wxiach.robot.pattern.GomokuShapeDetector;
import cn.wxiach.robot.pattern.Pattern;
import cn.wxiach.robot.pattern.ShapeDetector;


public abstract class AbstractEvaluator implements Evaluator {

    protected final ShapeDetector detector = GomokuShapeDetector.getInstance();

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
