package cn.wxiach.core.ai.pattern.feature;

import cn.wxiach.core.ai.pattern.Pattern;

import java.util.List;

public class FeaturePattern {

    // Create an unmodifiable pattern list by using List.of()
    // List.of() is ordered, so the order of the code is important
    private static final List<Pattern<String>> blackPatterns = List.of(
            new Pattern<>("11111", 50000),
            new Pattern<>("011110", 4320),
            new Pattern<>("011100", 720),
            new Pattern<>("001110", 720),
            new Pattern<>("011010", 720),
            new Pattern<>("010110", 720),
            new Pattern<>("11110", 720),
            new Pattern<>("01111", 720),
            new Pattern<>("11011", 720),
            new Pattern<>("10111", 720),
            new Pattern<>("11101", 720),
            new Pattern<>("001100", 120),
            new Pattern<>("001010", 120),
            new Pattern<>("010100", 120),
            new Pattern<>("001000", 20),
            new Pattern<>("000100", 20)
    );

    private static final List<Pattern<String>> whitePatterns = List.of(
            new Pattern<>("22222", 50000),
            new Pattern<>("022220", 4320),
            new Pattern<>("022200", 720),
            new Pattern<>("002220", 720),
            new Pattern<>("022020", 720),
            new Pattern<>("020220", 720),
            new Pattern<>("22220", 720),
            new Pattern<>("02222", 720),
            new Pattern<>("22022", 720),
            new Pattern<>("20222", 720),
            new Pattern<>("22202", 720),
            new Pattern<>("002200", 120),
            new Pattern<>("002020", 120),
            new Pattern<>("020200", 120),
            new Pattern<>("002000", 20),
            new Pattern<>("000200", 20)
    );

    private static final Pattern<String> defaultValue = new Pattern<>("", 0);

    public static List<Pattern<String>> getBlackPatterns() {
        return blackPatterns;
    }

    public static List<Pattern<String>> getWhitePatterns() {
        return whitePatterns;
    }

    public static Pattern<String> defaultValue() {
        return defaultValue;
    }
}
