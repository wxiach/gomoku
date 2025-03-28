package cn.wxiach.core.ai.evaluator;


import cn.wxiach.model.Color;


public abstract class Evaluator {

    protected final Color color;

    protected Evaluator(Color color) {
        this.color = color;
    }

    public int evaluate(int[][] board) {
        return evaluateMineScore(board) - evaluateOpponentScore(board);
    }

    abstract protected int evaluateMineScore(int[][] board);

    abstract protected int evaluateOpponentScore(int[][] board);
}
