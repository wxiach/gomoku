package cn.wxiach.core.ai.evaluator.feature;

import cn.wxiach.core.ai.evaluator.Evaluator;
import cn.wxiach.core.ai.pattern.Pattern;
import cn.wxiach.core.ai.pattern.feature.FeaturePatternDetector;
import cn.wxiach.core.ai.pattern.feature.PatternDetector;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;

import java.util.ArrayList;
import java.util.List;

public class FeatureBasedEvaluator extends Evaluator {

    public static final int[][] indexTable = new int[Board.BOARD_SIZE][Board.BOARD_SIZE];

    static {
        int count = 0;
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                indexTable[x][y] = count++;
            }
        }
    }

    private final List<int[]> lines = new ArrayList<>();

    public FeatureBasedEvaluator() {
        preStoreAllLines();
    }

    private void preStoreAllLines() {
        // row
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            this.lines.add(indexTable[x]);
        }

        // col
        for (int y = 0; y < Board.BOARD_SIZE; y++) {
            int[] line = new int[Board.BOARD_SIZE];
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                line[x] = indexTable[x][y];
            }
            this.lines.add(line);
        }

        // left diagonal
        for (int start = 0; start < Board.BOARD_SIZE * 2 - 1; start++) {
            List<Integer> numList = new ArrayList<>();
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                int y = start - x;
                if (y >= 0 && y < Board.BOARD_SIZE) {
                    numList.add(indexTable[x][y]);
                }
            }
            if (numList.size() >= 5) {
                this.lines.add(numList.stream().mapToInt(i -> i).toArray());
            }
        }

        // right diagonal
        for (int start = 0; start < Board.BOARD_SIZE * 2 - 1; start++) {
            List<Integer> numList = new ArrayList<>();
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                int y = start - (Board.BOARD_SIZE - 1 - x);
                if (y >= 0 && y < Board.BOARD_SIZE) {
                    numList.add(indexTable[x][y]);
                }
            }
            if (numList.size() >= 5) {
                this.lines.add(numList.stream().mapToInt(i -> i).toArray());
            }
        }
    }

    @Override
    protected int evaluateScore(char[][] board, Color color) {
        PatternDetector<String> patternDetector = new FeaturePatternDetector(color);
        return lines.stream()
                .map(line -> convertToLineStr(board, line))
                .map(patternDetector::detect)
                .mapToInt(Pattern::score)
                .sum();
    }

    private String convertToLineStr(char[][] board, int[] line) {
        char[] lineStr = new char[line.length];
        for (int i = 0; i < line.length; i++) {
            int x = line[i] / Board.BOARD_SIZE, y = line[i] % Board.BOARD_SIZE;
            lineStr[i] = board[x][y];
        }
        return new String(lineStr);
    }


}
