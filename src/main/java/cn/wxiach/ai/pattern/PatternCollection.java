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

    /*
     * Create an unmodifiable pattern list by using List.of()
     * List.of() is ordered, so the order of the code is important
     */
    public static final Pattern Five = new Pattern("11111", "A5", 100000);

    public static final Pattern aliveFour = new Pattern("011110", "A4", 6000);

    public static final List<Pattern> deathFour = List.of(
            new Pattern("11110", "D4", 600),
            new Pattern("01111", "D4", 600),
            new Pattern("11011", "D4", 600),
            new Pattern("10111", "D4", 600),
            new Pattern("11101", "D4", 600)
    );

    public static final List<Pattern> aliveThree = List.of(
            new Pattern("011100", "A3", 100),
            new Pattern("001110", "A3", 100),
            new Pattern("011010", "A3", 100),
            new Pattern("010110", "A3", 100)
    );

    public static final List<Pattern> aliveTwo = List.of(
            new Pattern("001100", "A2", 10),
            new Pattern("001010", "A2", 10),
            new Pattern("010100", "A2", 10)
    );

    public static final List<Pattern> aliveOne = List.of(
            new Pattern("001000", "A1", 1),
            new Pattern("000100", "A1", 1)
    );

    public static final List<Pattern> patterns = Stream.concat(
            Stream.of(Five, aliveFour),
            Stream.of(deathFour, aliveThree, aliveTwo, aliveOne).flatMap(List::stream)
    ).toList();
}

