package cn.wxiach.features.pattern;

public record Pattern(String pattern, String name, int value) implements Comparable<Pattern> {
    @Override
    public int compareTo(Pattern o) {
        return Integer.compare(this.value, o.value);
    }
}
