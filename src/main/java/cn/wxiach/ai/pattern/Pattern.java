package cn.wxiach.ai.pattern;

public record Pattern(String pattern, String name, int score) implements Comparable<Pattern> {
    @Override
    public int compareTo(Pattern o) {
        return Integer.compare(this.score, o.score);
    }
}
