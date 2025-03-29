package cn.wxiach;

import cn.wxiach.core.ai.pattern.Pattern;
import cn.wxiach.core.ai.pattern.support.AhoCorasickAutomaton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AhoCorasickAutomatonTest {

    private AhoCorasickAutomaton ahoCorasickAutomaton;

    @BeforeEach
    void setUp() {
        List<Pattern<String>> patterns = List.of(
                new Pattern<>("AAAA", 100),
                new Pattern<>("BBB", 50),
                new Pattern<>("CC", 20)
        );

        ahoCorasickAutomaton = new AhoCorasickAutomaton(patterns);
    }

    @Test
    void testSinglePatternMatch() {
        Set<Pattern<String>> matches = ahoCorasickAutomaton.search("XXAAAAXX");
        assertEquals(1, matches.size());
        assertTrue(matches.contains(new Pattern<>("AAAA", 100)));
    }

    @Test
    void testMultipleMatches() {
        Set<Pattern<String>> matches = ahoCorasickAutomaton.search("XXBBBCCAAAAXX");
        assertEquals(3, matches.size());
        assertTrue(matches.contains(new Pattern<>("AAAA", 100)));
        assertTrue(matches.contains(new Pattern<>("BBB", 50)));
        assertTrue(matches.contains(new Pattern<>("CC", 20)));
    }

    @Test
    void testNoMatch() {
        Set<Pattern<String>> matches = ahoCorasickAutomaton.search("XYZXYZXYZ");
        assertTrue(matches.isEmpty());
    }

}
