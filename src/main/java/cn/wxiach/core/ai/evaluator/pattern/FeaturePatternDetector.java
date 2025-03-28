package cn.wxiach.core.ai.evaluator.pattern;

import cn.wxiach.config.GomokuConf;
import cn.wxiach.model.Color;

import java.util.List;


public class FeaturePatternDetector {

    private final List<String> sortedPatterns;

    private final int[][] board;
    private final Color color;

    public FeaturePatternDetector(int[][] board, Color color) {
        this.board = board;
        this.color = color;
        sortedPatterns = FeaturePattern.getSortedPatterns();
    }

    public int detect(int[] line) {
        for (String pattern : sortedPatterns) {
            if (convertToLineStr(line).contains(pattern)) {
                return FeaturePattern.getScore(pattern);
            }
        }
        return 0;
    }

    private String convertToLineStr(int[] line) {
        StringBuilder lineStr = new StringBuilder();
        for (int pos : line) {
            int x = pos / GomokuConf.BOARD_SIZE, y = pos % GomokuConf.BOARD_SIZE;
            lineStr.append(pieceToCharacter(board[x][y], color));
        }
        return lineStr.toString();
    }

    private char pieceToCharacter(int value, Color color) {
        return switch (value) {
            case 0 -> '+';  // 空位
            case 1 -> (color.getValue() == Color.BLACK.getValue() ? 'O' : 'X');  // 己方
            case 2 -> (color.getValue() == Color.BLACK.getValue() ? 'X' : 'O');  // 对方
            default -> throw new IllegalArgumentException("Unexpected value: " + value);
        };
    }

}
