package cn.wxiach.features.pattern;

import cn.wxiach.model.Board;
import cn.wxiach.model.Point;

import java.util.Collection;

/**
 * @author wxiach 2025/5/5
 */
public class MatchLineDetector {

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
