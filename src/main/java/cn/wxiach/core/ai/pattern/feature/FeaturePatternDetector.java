package cn.wxiach.core.ai.pattern.feature;

import cn.wxiach.core.ai.pattern.Pattern;
import cn.wxiach.core.ai.pattern.support.AhoCorasickAutomaton;
import cn.wxiach.model.Color;

import java.util.Comparator;


public class FeaturePatternDetector implements PatternDetector<String> {

    private final AhoCorasickAutomaton ahoCorasickAutomaton;

    public FeaturePatternDetector(Color color) {
        ahoCorasickAutomaton = (color == Color.WHITE)
                ? new AhoCorasickAutomaton(FeaturePattern.getWhitePatterns())
                : new AhoCorasickAutomaton(FeaturePattern.getBlackPatterns());
    }

    @Override
    public Pattern<String> detect(String line) {
        return ahoCorasickAutomaton.search(line).stream()
                .max(Comparator.naturalOrder())
                .orElse(FeaturePattern.defaultValue());
    }
}
