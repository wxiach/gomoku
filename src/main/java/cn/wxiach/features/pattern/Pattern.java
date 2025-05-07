package cn.wxiach.features.pattern;

public record Pattern(PatternName name, String pattern, int value) implements Comparable<Pattern> {
    @Override
    public int compareTo(Pattern o) {
        return Integer.compare(this.value, o.value);
    }
}
