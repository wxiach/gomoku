package cn.wxiach.ai.pattern.feature;


import cn.wxiach.ai.pattern.Detector;
import cn.wxiach.ai.pattern.Pattern;
import cn.wxiach.model.Point;

import java.util.Collection;

public class FeatureDetector implements Detector<String> {

    private static FeatureDetector instance;

    public static FeatureDetector getInstance() {
        if (instance == null) {
            instance = new FeatureDetector();
        }
        return instance;
    }

    protected FeatureMatchLineDetector matchLineDetector = new FeatureMatchLineDetector();
    protected FeaturePatternDetector patternDetector = new FeaturePatternDetector();

    @Override
    public Collection<Pattern<String>> detect(char[][] board) {
        return patternDetector.detect(matchLineDetector.detect(board));
    }

    @Override
    public Collection<Pattern<String>> detect(char[][] board, Point point) {
        return patternDetector.detect(matchLineDetector.detect(board, point));
    }
}
