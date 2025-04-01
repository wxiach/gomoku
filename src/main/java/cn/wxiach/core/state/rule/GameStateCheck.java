package cn.wxiach.core.state.rule;


import cn.wxiach.core.ai.pattern.Pattern;
import cn.wxiach.core.ai.pattern.feature.FeaturePattern;
import cn.wxiach.core.ai.pattern.feature.FeaturePatternDetector;
import cn.wxiach.model.Color;

public interface GameStateCheck {
    Pattern<String> BLACK_WIN_CONDITION = FeaturePattern.getBlackPatterns().getFirst();
    Pattern<String> WHITE_WIN_CONDITION = FeaturePattern.getWhitePatterns().getFirst();
    int WIN_SCORE = BLACK_WIN_CONDITION.score();

    FeaturePatternDetector featurePatternDetector = FeaturePatternDetector.getInstance();

    static boolean isOver(char[][] board) {
        return featurePatternDetector.detect(board, Color.WHITE).contains(WHITE_WIN_CONDITION)
                || featurePatternDetector.detect(board, Color.BLACK).contains(BLACK_WIN_CONDITION);
    }
}
