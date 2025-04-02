package cn.wxiach.ai.pattern.feature;

import cn.wxiach.ai.pattern.Pattern;
import cn.wxiach.ai.pattern.PatternCollection;

import java.util.Collection;
import java.util.List;


public class FeaturePatternCollection implements PatternCollection<String> {

    /*
     * Create an unmodifiable pattern list by using List.of()
     * List.of() is ordered, so the order of the code is important
     */
    private final List<Pattern<String>> patterns = List.of(
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

    private final Pattern<String> win = new Pattern<>("11111", 50000);

    private final Pattern<String> none = new Pattern<>("", 0);

    @Override
    public Pattern<String> empty() {
        return none;
    }

    @Override
    public Pattern<String> win() {
        return win;
    }

    @Override
    public Collection<Pattern<String>> patterns() {
        return patterns;
    }

}
