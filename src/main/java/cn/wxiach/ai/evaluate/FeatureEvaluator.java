package cn.wxiach.ai.evaluate;


import cn.wxiach.model.Board;
import cn.wxiach.model.Color;

public interface FeatureEvaluator {

    /**
     * Evaluates the board's score, taking the opponent's score into account.
     *
     * @param board
     * @param color Determines whether the board is evaluated from the perspective of Black or White.
     * @return
     */
    int evaluate(Board board, Color color);
}
