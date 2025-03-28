package cn.wxiach.core.ai.evaluator;

import cn.wxiach.config.GomokuConf;
import cn.wxiach.core.ai.evaluator.pattern.FeaturePatternDetector;
import cn.wxiach.core.state.PieceColorState;
import cn.wxiach.model.Color;

import java.util.ArrayList;
import java.util.List;

public class FeatureBasedEvaluator extends Evaluator {

    public static final int[][] indexTable = new int[GomokuConf.BOARD_SIZE][GomokuConf.BOARD_SIZE];

    static {
        int count = 0;
        for (int x = 0; x < GomokuConf.BOARD_SIZE; x++) {
            for (int y = 0; y < GomokuConf.BOARD_SIZE; y++) {
                indexTable[x][y] = count++;
            }
        }
    }

    private final List<int[]> lines = new ArrayList<>();

    public FeatureBasedEvaluator(Color color) {
        super(color);
        preStoreAllLines();
    }

    private void preStoreAllLines() {
        // row
        for (int x = 0; x < GomokuConf.BOARD_SIZE; x++) {
            this.lines.add(indexTable[x]);
        }

        // col
        for (int y = 0; y < GomokuConf.BOARD_SIZE; y++) {
            int[] line = new int[GomokuConf.BOARD_SIZE];
            for (int x = 0; x < GomokuConf.BOARD_SIZE; x++) {
                line[x] = indexTable[x][y];
            }
            this.lines.add(line);
        }

        // left diagonal
        for (int start = 0; start < GomokuConf.BOARD_SIZE * 2 - 1; start++) {
            List<Integer> numList = new ArrayList<>();
            for (int x = 0; x < GomokuConf.BOARD_SIZE; x++) {
                int y = start - x;
                if (y >= 0 && y < GomokuConf.BOARD_SIZE) {
                    numList.add(indexTable[x][y]);
                }
            }
            if (numList.size() >= 5) {
                this.lines.add(numList.stream().mapToInt(i -> i).toArray());
            }
        }

        // right diagonal
        for (int start = 0; start < GomokuConf.BOARD_SIZE * 2 - 1; start++) {
            List<Integer> numList = new ArrayList<>();
            for (int x = 0; x < GomokuConf.BOARD_SIZE; x++) {
                int y = start - (GomokuConf.BOARD_SIZE - 1 - x);
                if (y >= 0 && y < GomokuConf.BOARD_SIZE) {
                    numList.add(indexTable[x][y]);
                }
            }
            if (numList.size() >= 5) {
                this.lines.add(numList.stream().mapToInt(i -> i).toArray());
            }
        }
    }

    @Override
    protected int evaluateMineScore(int[][] board) {
        FeaturePatternDetector featurePatternDetector = new FeaturePatternDetector(board, color);
        return lines.stream().mapToInt(featurePatternDetector::detect).sum();
    }

    @Override
    protected int evaluateOpponentScore(int[][] board) {
        // Obtain the opponent piece color
        Color color = PieceColorState.reverseColor(this.color);
        FeaturePatternDetector featurePatternDetector = new FeaturePatternDetector(board, color);
        return lines.stream().mapToInt(featurePatternDetector::detect).sum();
    }
}
