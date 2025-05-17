package cn.wxiach.pattern;

import java.util.Comparator;
import java.util.Optional;

/**
 * @author wxiach 2025/5/10
 */
public enum PatternMatcherUtils {
		INSTANCE;

		private final AhoCorasickAutomaton<Pattern> blackAutomaton;
		private final AhoCorasickAutomaton<Pattern> whiteAutomaton;

		PatternMatcherUtils() {
				blackAutomaton = new AhoCorasickAutomaton<>(Patterns.ALL, Pattern::black);
				whiteAutomaton = new AhoCorasickAutomaton<>(Patterns.ALL, Pattern::white);
		}

		public static Optional<Pattern> matchWhitePattern(String text) {
				return INSTANCE.whiteAutomaton.search(text).stream().max(Comparator.naturalOrder());
		}

		public static Optional<Pattern> matchBlackPattern(String text) {
				return INSTANCE.blackAutomaton.search(text).stream().max(Comparator.naturalOrder());
		}
}
