package cn.wxiach.features;


import cn.wxiach.features.pattern.MatchLineDetector;
import cn.wxiach.features.pattern.Pattern;
import cn.wxiach.features.pattern.PatternDetector;
import cn.wxiach.model.Board;
import cn.wxiach.model.Point;

import java.util.Collection;

public class BoardFeatureDetector implements ShapeDetector {

    private static BoardFeatureDetector instance;

    public static BoardFeatureDetector getInstance() {
        if (instance == null) {
            instance = new BoardFeatureDetector();
        }
        return instance;
    }

    private final MatchLineDetector matchLineDetector = new MatchLineDetector();
    private final PatternDetector patternDetector = new PatternDetector();

    @Override
    public Collection<Pattern> detect(Board board) {
        return patternDetector.detect(matchLineDetector.detect(board));
    }

    @Override
    public Collection<Pattern> detect(Board board, Point point) {
        return patternDetector.detect(matchLineDetector.detect(board, point));
    }
}
