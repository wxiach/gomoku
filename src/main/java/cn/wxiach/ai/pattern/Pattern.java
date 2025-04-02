package cn.wxiach.ai.pattern;

public record Pattern<T>(T pattern, int score) implements Comparable<Pattern<T>> {
    @Override
    public int compareTo(Pattern o) {
        return Integer.compare(this.score, o.score);
    }
}
