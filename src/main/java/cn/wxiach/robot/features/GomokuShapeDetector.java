package cn.wxiach.robot.features;


import cn.wxiach.model.Board;
import cn.wxiach.model.Point;
import cn.wxiach.robot.support.AhoCorasickAutomaton;
import cn.wxiach.robot.support.BoardIndexTable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public class GomokuShapeDetector implements ShapeDetector {

    private static GomokuShapeDetector instance;

    public static GomokuShapeDetector getInstance() {
        if (instance == null) {
            instance = new GomokuShapeDetector();
        }
        return instance;
    }

    private final MatchLineDetector matchLineDetector = new MatchLineDetector();
    private final PatternDetector patternDetector = new PatternDetector();

    @Override
    public Collection<Pattern> detect(Board board) {
        return patternDetector.detect(matchLineDetector.detect(board));
    }

    @Override
    public Collection<Pattern> detect(Board board, Point point) {
        return patternDetector.detect(matchLineDetector.detect(board, point));
    }

    private static class MatchLineDetector {

        private final BoardIndexTable boardIndexTable = new BoardIndexTable();

        public Collection<String> detect(Board board) {
            return boardIndexTable.indexLine().stream()
                    .map(indexLine -> extractLineStringFromIndices(board, indexLine)).toList();
        }

        public Collection<String> detect(Board board, Point point) {
            return boardIndexTable.indexLine(point.x(), point.y()).stream()
                    .map(indexLine -> extractLineStringFromIndices(board, indexLine)).toList();
        }

        private String extractLineStringFromIndices(Board board, int[] indexLine) {
            char[] line = new char[indexLine.length];
            for (int i = 0; i < indexLine.length; i++) line[i] = board.get(indexLine[i]);
            return new String(line);
        }
    }


    private static class PatternDetector {

        private final AhoCorasickAutomaton<Pattern> acAutomaton =
                new AhoCorasickAutomaton<>(PatternCollection.PATTERNS, Pattern::pattern);

        public Collection<Pattern> detect(Collection<String> matchLines) {
            return matchLines.stream().map(this::detectSingleLine)
                    .filter(pattern -> Objects.nonNull(pattern) && !pattern.pattern().isBlank()).toList();
        }

        private Pattern detectSingleLine(String matchLine) {
            return acAutomaton.search(matchLine).stream().max(Comparator.naturalOrder()).orElse(null);
        }
    }
}
