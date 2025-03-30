package cn.wxiach.core.ai.pattern.feature;

import cn.wxiach.model.Board;

public class FeatureLine {
    private final int[] index;
    private String value;

    public FeatureLine(int[] index) {
        this.index = index;
    }

    public void setValue(char[][] board) {
        value = extractLineStringFromIndices(board, index);
    }

    public String value() {
        return this.value;
    }

    private String extractLineStringFromIndices(char[][] board, int[] index) {
        char[] line = new char[index.length];
        for (int i = 0; i < index.length; i++) {
            line[i] = board[index[i] / Board.SIZE][index[i] % Board.SIZE];
        }
        return new String(line);
    }
}
