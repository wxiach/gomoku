package cn.wxiach.ai.pattern;

import cn.wxiach.model.Board;
import cn.wxiach.model.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class BoardIndexTable {

    private static final int[][] indexTable = new int[Board.SIZE][Board.SIZE];

    static {
        int count = 0;
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                indexTable[x][y] = count++;
            }
        }
    }

    private final List<int[]> hIndex = new ArrayList<>();
    private final List<int[]> vIndex = new ArrayList<>();
    private final List<int[]> lIndex = new ArrayList<>();
    private final List<int[]> rIndex = new ArrayList<>();

    public BoardIndexTable() {
        preStoreAllLines();
    }

    public List<int[]> index() {
        return Stream.of(hIndex, vIndex, lIndex, rIndex)
                .flatMap(Collection::stream)
                .toList();
    }

    public List<int[]> index(Point point) {
        List<int[]> lines = new ArrayList<>();
        lines.add(hIndex.get(point.x()));
        lines.add(vIndex.get(point.y()));

        int arrIndex = point.x() + point.y() - 4;
        if (arrIndex <= 20) lines.add(lIndex.get(arrIndex));

        arrIndex = point.x() - point.y() + (Board.SIZE-5);
        if (arrIndex <= 20) lines.add(rIndex.get(arrIndex));

        return Collections.unmodifiableList(lines);
    }

    private void preStoreAllLines() {
        // horizontal
        for (int x = 0; x < Board.SIZE; x++) {
            hIndex.add(indexTable[x]);
        }

        // vertical
        for (int y = 0; y < Board.SIZE; y++) {
            int[] line = new int[Board.SIZE];
            for (int x = 0; x < Board.SIZE; x++) {
                line[x] = indexTable[x][y];
            }
            vIndex.add(line);
        }

        // left diagonal ( / direction, top left to bottom right)
        for (int start = 0; start < Board.SIZE * 2 - 1; start++) {
            List<Integer> numList = new ArrayList<>();
            for (int x = 0; x < Board.SIZE; x++) {
                int y = start - x;
                if (y >= 0 && y < Board.SIZE) {
                    numList.add(indexTable[x][y]);
                }
            }
            if (numList.size() >= 5) {
                lIndex.add(numList.stream().mapToInt(i -> i).toArray());
            }
        }

        // right diagonal ( \ direction, bottom left to top right)
        for (int start = 0; start < Board.SIZE * 2 - 1; start++) {
            List<Integer> numList = new ArrayList<>();
            for (int x = 0; x < Board.SIZE; x++) {
                int y = start - (Board.SIZE - 1 - x);
                if (y >= 0 && y < Board.SIZE) {
                    numList.add(indexTable[x][y]);
                }
            }
            if (numList.size() >= 5) {
                rIndex.add(numList.stream().mapToInt(i -> i).toArray());
            }
        }
    }
}
