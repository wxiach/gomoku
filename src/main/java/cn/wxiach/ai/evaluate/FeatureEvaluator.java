package cn.wxiach.ai.evaluate;

import cn.wxiach.ai.pattern.Pattern;
import cn.wxiach.ai.pattern.feature.FeatureDetector;

public class FeatureEvaluator implements Evaluator {

    @Override
    public int evaluate(char[][] board) {
        return FeatureDetector.getInstance().detect(board).stream().mapToInt(Pattern::score).sum();
    }
}
