package cn.wxiach;

import cn.wxiach.robot.support.AhoCorasickAutomaton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AhoCorasickAutomatonTest {

    private AhoCorasickAutomaton<String> automaton;

    private static class StringConverter implements AhoCorasickAutomaton.CharSequenceConverter<String> {
        @Override
        public CharSequence toCharSequence(String s) {
            return s;
        }
    }

    @BeforeEach
    void setUp() {
        automaton = new AhoCorasickAutomaton<>(Arrays.asList("he", "she", "his", "hers"), new StringConverter());
    }

    @Test
    void testInsertAndSearch() {
        Set<String> result = automaton.search("ushers");
        Set<String> expected = new HashSet<>(Arrays.asList("she", "he", "hers"));
        assertEquals(expected, result, "Search should find correct patterns");
    }

    @Test
    void testNoMatch() {
        Set<String> result = automaton.search("randomtext");
        assertTrue(result.isEmpty(), "Search should return empty set when no matches found");
    }

    @Test
    void testSinglePatternMatch() {
        Set<String> result = automaton.search("his");
        assertTrue(result.contains("his"), "Search should detect exact pattern");
    }
}



