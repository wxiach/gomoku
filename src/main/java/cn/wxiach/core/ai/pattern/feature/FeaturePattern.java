package cn.wxiach.core.ai.pattern.feature;

import cn.wxiach.core.ai.pattern.Pattern;

import java.util.List;

public class FeaturePattern {

    private static final List<Pattern<String>> patterns = List.of(
            new Pattern<>("OOOOO", 50000),
            new Pattern<>("+OOOO+", 4320),
            new Pattern<>("+OOO++", 720),
            new Pattern<>("++OOO+", 720),
            new Pattern<>("+OO+O+", 720),
            new Pattern<>("+O+OO+", 720),
            new Pattern<>("OOOO+", 720),
            new Pattern<>("+OOOO", 720),
            new Pattern<>("OO+OO", 720),
            new Pattern<>("O+OOO", 720),
            new Pattern<>("OOO+O", 720),
            new Pattern<>("++OO++", 120),
            new Pattern<>("++O+O+", 120),
            new Pattern<>("+O+O++", 120),
            new Pattern<>("++O+++", 20),
            new Pattern<>("+++O++", 20)
    );

    public static List<Pattern<String>> getPatterns() {
        return patterns;
    }
}
