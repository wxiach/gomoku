package cn.wxiach.pattern;

/**
 * @param name  棋形名称
 * @param black 黑棋模式串
 * @param white 白棋模式串
 * @param value 估值
 */
public record Pattern(PatternName name, String black, String white, int value) implements Comparable<Pattern> {
		@Override
		public int compareTo(Pattern o) {
				return Integer.compare(this.value, o.value);
		}
}
