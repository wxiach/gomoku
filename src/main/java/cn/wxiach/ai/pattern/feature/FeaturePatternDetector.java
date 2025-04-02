package cn.wxiach.ai.pattern.feature;

import cn.wxiach.ai.pattern.Pattern;
import cn.wxiach.ai.pattern.PatternDetector;
import cn.wxiach.ai.support.AhoCorasickAutomaton;

import java.util.Collection;
import java.util.Comparator;

public class FeaturePatternDetector implements PatternDetector<String> {

    private final FeaturePatternCollection patternCollection = new FeaturePatternCollection();
    private final AhoCorasickAutomaton<Pattern<String>> ahoCorasickAutomaton =
            new AhoCorasickAutomaton<>(patternCollection.patterns(), Pattern::pattern);

    @Override
    public Collection<Pattern<String>> detect(Collection<String> matchLines) {
        return matchLines.stream()
                .map(this::detect)
                .filter(pattern -> !pattern.pattern().isBlank())
                .toList();
    }

    @Override
    public Pattern<String> detect(String matchLine) {
        return ahoCorasickAutomaton.search(matchLine).stream()
                .max(Comparator.naturalOrder())
                .orElse(patternCollection.empty());
    }
}
