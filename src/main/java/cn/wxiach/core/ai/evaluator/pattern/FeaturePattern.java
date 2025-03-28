package cn.wxiach.core.ai.evaluator.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FeaturePattern {
    private static final Map<String, Integer> scores = new ConcurrentHashMap<>();

    static {
        scores.put("OOOOO", 50000);
        scores.put("+OOOO+", 4320);
        scores.put("+OOO+", 720);
        scores.put("++OOO++", 720);
        scores.put("+O+OO+", 720);
        scores.put("+OO+O+", 720);
        scores.put("OOOO+", 720);
        scores.put("+OOOO", 720);
        scores.put("OO+OO", 720);
        scores.put("O+OOO", 720);
        scores.put("OOO+O", 720);
        scores.put("++OO++", 120);
        scores.put("+O+O+O+", 120);
        scores.put("+OO+O++", 120);
        scores.put("++O+O++", 120);
        scores.put("++++O+++", 20);
    }

    public static List<String> getSortedPatterns() {
        return scores.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }


    public static int getScore(String pattern) {
        return scores.getOrDefault(pattern, 0);
    }

}
