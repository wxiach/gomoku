package cn.wxiach.core.ai.pattern.feature;

import cn.wxiach.config.GomokuConf;
import cn.wxiach.core.ai.pattern.support.AhoCorasickAutomaton;
import cn.wxiach.core.ai.pattern.Pattern;
import cn.wxiach.model.Color;

import java.util.Comparator;


public class FeaturePatternDetector {

    private final AhoCorasickAutomaton ahoCorasickAutomaton = new AhoCorasickAutomaton(FeaturePattern.getPatterns());

    private final int[][] board;
    private final Color color;

    public FeaturePatternDetector(int[][] board, Color color) {
        this.board = board;
        this.color = color;
    }

    public int detect(int[] line) {
        return ahoCorasickAutomaton.search(convertToLineStr(line)).stream()
                .max(Comparator.naturalOrder())
                .map(Pattern::score)
                .orElse(0);
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
