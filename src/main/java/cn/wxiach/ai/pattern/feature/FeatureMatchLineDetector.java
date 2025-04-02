package cn.wxiach.ai.pattern.feature;

import cn.wxiach.ai.pattern.BoardIndexTable;
import cn.wxiach.ai.pattern.MatchLineDetector;
import cn.wxiach.model.Board;
import cn.wxiach.model.Point;

import java.util.Collection;

public class FeatureMatchLineDetector implements MatchLineDetector<String> {

    private final BoardIndexTable boardIndexTable = new BoardIndexTable();

    @Override
    public Collection<String> detect(char[][] board) {
        return boardIndexTable.index().stream()
                .map(index -> extractLineStringFromIndices(board, index))
                .toList();
    }

    @Override
    public Collection<String> detect(char[][] board, Point point) {
        return boardIndexTable.index(point).stream()
                .map(index -> extractLineStringFromIndices(board, index))
                .toList();
    }

    private String extractLineStringFromIndices(char[][] board, int[] index) {
        char[] line = new char[index.length];
        for (int i = 0; i < index.length; i++) {
            line[i] = board[index[i] / Board.SIZE][index[i] % Board.SIZE];
        }
        return new String(line);
    }
}
