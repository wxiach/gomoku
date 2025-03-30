package cn.wxiach.core.ai.pattern.feature;

import cn.wxiach.model.Board;
import cn.wxiach.model.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class FeaturePatternIndexTableProvider {

    private static final int[][] indexTable = new int[Board.BOARD_SIZE][Board.BOARD_SIZE];

    static {
        int count = 0;
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int y = 0; y < Board.BOARD_SIZE; y++) {
                indexTable[x][y] = count++;
            }
        }
    }

    private final List<FeatureLine> horizontalIndexLines = new ArrayList<>();
    private final List<FeatureLine> verticalIndexLines = new ArrayList<>();
    private final List<FeatureLine> leftDiagonalIndexLines = new ArrayList<>();
    private final List<FeatureLine> rightDiagonalIndexLines = new ArrayList<>();


    public FeaturePatternIndexTableProvider() {
        preStoreAllLines();
    }

    public List<FeatureLine> indexLines() {
        return Stream.of(horizontalIndexLines, verticalIndexLines, leftDiagonalIndexLines, rightDiagonalIndexLines)
                .flatMap(Collection::stream)
                .toList();
    }

    public List<FeatureLine> indexLines(Point point) {
        return Collections.emptyList();
    }

    private void preStoreAllLines() {
        // horizontal
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
           horizontalIndexLines.add(new FeatureLine(indexTable[x]));
        }

        // vertical
        for (int y = 0; y < Board.BOARD_SIZE; y++) {
            int[] line = new int[Board.BOARD_SIZE];
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                line[x] = indexTable[x][y];
            }
            verticalIndexLines.add(new FeatureLine(line));
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
                leftDiagonalIndexLines.add(new FeatureLine(numList.stream().mapToInt(i -> i).toArray()));
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
                rightDiagonalIndexLines.add(new FeatureLine(numList.stream().mapToInt(i -> i).toArray()));
            }
        }
    }
}
