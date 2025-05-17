package cn.wxiach.pattern;

import java.util.List;
import java.util.stream.Stream;

public class Patterns {

		public static final Pattern A5 = new Pattern(PatternName.A5, "11111", "22222", 50000);
		public static final Pattern A4 = new Pattern(PatternName.A4, "011110", "022220", 4320);

		public static final List<Pattern> D4 = List.of(
						new Pattern(PatternName.D4, "11110", "22220", 720),
						new Pattern(PatternName.D4, "01111", "02222", 720),
						new Pattern(PatternName.D4, "11011", "22022", 720),
						new Pattern(PatternName.D4, "10111", "20222", 720),
						new Pattern(PatternName.D4, "11101", "22202", 720)
		);

		public static final List<Pattern> A3 = List.of(
						new Pattern(PatternName.A3, "011100", "022200", 720),
						new Pattern(PatternName.A3, "001110", "002220", 720),
						new Pattern(PatternName.A3, "011010", "022020", 720),
						new Pattern(PatternName.A3, "010110", "020220", 720)
		);

		public static final List<Pattern> A2 = List.of(
						new Pattern(PatternName.A2, "001100", "002200", 120),
						new Pattern(PatternName.A2, "001010", "002020", 120),
						new Pattern(PatternName.A2, "010100", "020200", 120)
		);

		public static final List<Pattern> A1 = List.of(
						new Pattern(PatternName.A1, "001000", "002000", 20),
						new Pattern(PatternName.A1, "000100", "000200", 20)
		);

		public static final Pattern none = new Pattern(null, "00000", "00000", 0);

		public static final List<Pattern> ALL = Stream.concat(
						Stream.of(A5, A4),
						Stream.of(D4, A3, A2, A1).flatMap(List::stream)
		).toList();
}


