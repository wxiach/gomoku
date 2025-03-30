package cn.wxiach.core.ai.evaluator;

import cn.wxiach.core.ai.pattern.Pattern;
import cn.wxiach.core.ai.pattern.feature.FeaturePatternDetector;
import cn.wxiach.core.ai.pattern.feature.PatternDetector;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;


public class FeatureBasedEvaluator implements Evaluator {

    private final PatternDetector<String> patternDetector = FeaturePatternDetector.getInstance();

    @Override
    public int evaluateForColor(char[][] board, Color color) {
        return patternDetector.detect(board, color).stream().mapToInt(Pattern::score).sum();
    }

    @Override
    public int evaluatePieceOnLineForColor(char[][] board, Piece piece) {
        return patternDetector.detect(board, piece).stream().mapToInt(Pattern::score).sum();
    }

}
