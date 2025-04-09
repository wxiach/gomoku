package cn.wxiach.ai.pattern;

import java.util.List;
import java.util.stream.Stream;


public class PatternCollection {

    public static final String A5 = "A5";
    public static final String A4 = "A4";
    public static final String D4 = "D4";
    public static final String A3 = "A3";
    public static final String A2 = "A2";
    public static final String A1 = "A1";
    public static final String NONE = "none";

    public static final int A5_VALUE = 50000;
    public static final int A4_VALUE = 4320;
    public static final int D4_VALUE = 720;
    public static final int A3_VALUE = 720;
    public static final int A2_VALUE = 120;
    public static final int A1_VALUE = 20;

    /*
     * Create an unmodifiable pattern list by using List.of()
     * List.of() is ordered, so the order of the code is important
     */
    public static final Pattern FIVE = new Pattern("11111", A5, A5_VALUE);

    public static final Pattern ALIVE_FOUR = new Pattern("011110", A4, A4_VALUE);

    public static final List<Pattern> DEATH_FOUR = List.of(
            new Pattern("11110", D4, D4_VALUE),
            new Pattern("01111", D4, D4_VALUE),
            new Pattern("11011", D4, D4_VALUE),
            new Pattern("10111", D4, D4_VALUE),
            new Pattern("11101", D4, D4_VALUE)
    );

    public static final List<Pattern> ALIVE_THREE = List.of(
            new Pattern("011100", A3, A3_VALUE),
            new Pattern("001110", A3, A3_VALUE),
            new Pattern("011010", A3, A3_VALUE),
            new Pattern("010110", A3, A3_VALUE)
    );

    public static final List<Pattern> ALIVE_TWO = List.of(
            new Pattern("001100", A2, A2_VALUE),
            new Pattern("001010", A2, A2_VALUE),
            new Pattern("010100", A2, A2_VALUE)
    );

    public static final List<Pattern> ALIVE_ONE = List.of(
            new Pattern("001000", A1, A1_VALUE),
            new Pattern("000100", A1, A1_VALUE)
    );

    public static final Pattern NONE_PATTERN = new Pattern("", NONE, 0);

    public static final List<Pattern> PATTERNS = Stream.concat(
            Stream.of(FIVE, ALIVE_FOUR),
            Stream.of(DEATH_FOUR, ALIVE_THREE, ALIVE_TWO, ALIVE_ONE).flatMap(List::stream)
    ).toList();
}


