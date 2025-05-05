package cn.wxiach.features.pattern;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

/**
 * @author wxiach 2025/5/5
 */
public class PatternDetector {
    private final AhoCorasickAutomaton<Pattern> acAutomaton =
            new AhoCorasickAutomaton<>(PatternCollection.PATTERNS, Pattern::pattern);

    public Collection<Pattern> detect(Collection<String> matchLines) {
        return matchLines.stream().map(this::detectSingleLine)
                .filter(pattern -> Objects.nonNull(pattern) && !pattern.pattern().isBlank()).toList();
    }

    private Pattern detectSingleLine(String matchLine) {
        return acAutomaton.search(matchLine).stream().max(Comparator.naturalOrder()).orElse(null);
    }
}
