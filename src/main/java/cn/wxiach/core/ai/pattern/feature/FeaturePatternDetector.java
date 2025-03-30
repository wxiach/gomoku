package cn.wxiach.core.ai.pattern.feature;

import cn.wxiach.core.ai.pattern.Pattern;
import cn.wxiach.core.ai.pattern.support.AhoCorasickAutomatonFactory;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.model.Point;

import java.util.Comparator;
import java.util.List;


public class FeaturePatternDetector extends FeaturePatternIndexTableProvider implements PatternDetector<String> {

    private static FeaturePatternDetector featurePatternDetector;

    public static FeaturePatternDetector getInstance() {
        if (featurePatternDetector == null) {
            featurePatternDetector = new FeaturePatternDetector();
        }
        return featurePatternDetector;
    }


    @Override
    public Pattern<String> detect(FeatureLine line, Color color) {
        return AhoCorasickAutomatonFactory.getInstance(color).search(line.value())
                .stream()
                .max(Comparator.naturalOrder())
                .orElse(FeaturePattern.defaultValue());
    }

    @Override
    public List<Pattern<String>> detect(char[][] board, Piece piece) {
        return lines(board, piece.point()).parallelStream().map(line -> detect(line, piece.color())).toList();
    }


    @Override
    public List<Pattern<String>> detect(char[][] board, Color color) {
        return lines(board).parallelStream().map(line -> detect(line, color)).toList();
    }


    private List<FeatureLine> lines(char[][] board) {
        return indexLines().stream().peek(line -> line.setValue(board)).toList();
    }

    private List<FeatureLine> lines(char[][] board, Point point) {
        return indexLines(point).stream().peek(line -> line.setValue(board)).toList();
    }
}
