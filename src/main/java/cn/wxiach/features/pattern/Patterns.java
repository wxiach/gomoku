package cn.wxiach.features.pattern;

import java.util.List;
import java.util.stream.Stream;

public class Patterns {

    public static final Pattern A5 = new Pattern(PatternName.A5, "11111", 50000);
    public static final Pattern A4 = new Pattern(PatternName.A4, "011110", 4320);

    public static final List<Pattern> D4 = List.of(
            new Pattern(PatternName.D4, "11110", 720),
            new Pattern(PatternName.D4, "01111", 720),
            new Pattern(PatternName.D4, "11011", 720),
            new Pattern(PatternName.D4, "10111", 720),
            new Pattern(PatternName.D4, "11101", 720)
    );

    public static final List<Pattern> A3 = List.of(
            new Pattern(PatternName.A3, "011100", 720),
            new Pattern(PatternName.A3, "001110", 720),
            new Pattern(PatternName.A3, "011010", 720),
            new Pattern(PatternName.A3, "010110", 720)
    );

    public static final List<Pattern> A2 = List.of(
            new Pattern(PatternName.A2, "001100", 120),
            new Pattern(PatternName.A2, "001010", 120),
            new Pattern(PatternName.A2, "010100", 120)
    );

    public static final List<Pattern> A1 = List.of(
            new Pattern(PatternName.A1, "001000", 20),
            new Pattern(PatternName.A1, "000100", 20)
    );

    public static final List<Pattern> PATTERNS = Stream.concat(
            Stream.of(A5, A4),
            Stream.of(D4, A3, A2, A1).flatMap(List::stream)
    ).toList();
}


