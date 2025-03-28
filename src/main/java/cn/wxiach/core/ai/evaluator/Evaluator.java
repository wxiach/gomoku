package cn.wxiach.core.ai.evaluator;


import cn.wxiach.core.state.PieceColorState;
import cn.wxiach.model.Color;

public abstract class Evaluator {

    public int evaluate(char[][] board, Color color) {
        return evaluateScore(board, color) - evaluateScore(board, PieceColorState.reverseColor(color));
    }

    abstract protected int evaluateScore(char[][] board, Color color);
}
