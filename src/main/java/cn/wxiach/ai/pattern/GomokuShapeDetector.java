package cn.wxiach.ai.pattern;


import cn.wxiach.ai.support.AhoCorasickAutomaton;
import cn.wxiach.ai.support.BoardIndexTable;

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
    public Collection<Pattern> detect(char[][] board) {
        return patternDetector.detect(matchLineDetector.detect(board));
    }

    @Override
    public Collection<Pattern> detect(char[][] board, int x, int y) {
        return patternDetector.detect(matchLineDetector.detect(board, x, y));
    }


    private static class MatchLineDetector {

        private final BoardIndexTable boardIndexTable = new BoardIndexTable();

        public Collection<String> detect(char[][] board) {
            return boardIndexTable.indexLine().stream()
                    .map(indexLine -> extractLineStringFromIndices(board, indexLine)).toList();
        }

        public Collection<String> detect(char[][] board, int x, int y) {
            return boardIndexTable.indexLine(x, y).stream()
                    .map(indexLine -> extractLineStringFromIndices(board, indexLine)).toList();
        }

        private String extractLineStringFromIndices(char[][] board, int[] indexLine) {
            char[] line = new char[indexLine.length];
            for (int i = 0; i < indexLine.length; i++) {
                line[i] = board[indexLine[i] % board.length][indexLine[i] / board.length];
            }
            return new String(line);
        }
    }


    private static class PatternDetector {

        private final AhoCorasickAutomaton<Pattern> acAutomaton =
                new AhoCorasickAutomaton<>(PatternCollection.patterns, Pattern::pattern);


        public Collection<Pattern> detect(Collection<String> matchLines) {
            return matchLines.stream().map(this::detectSingleLine)
                    .filter(pattern -> Objects.nonNull(pattern) && !pattern.pattern().isBlank()).toList();
        }

        private Pattern detectSingleLine(String matchLine) {
            return acAutomaton.search(matchLine).stream().max(Comparator.naturalOrder()).orElse(null);
        }
    }
}
