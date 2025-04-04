package cn.wxiach.ai.support;

import cn.wxiach.model.Board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class BoardIndexTable {

    private final static int indexTableSize = Board.SIZE;
    private static final int[][] indexTable = new int[indexTableSize][indexTableSize];

    static {
        int count = 0;
        for (int x = 0; x < indexTableSize; x++) {
            for (int y = 0; y < indexTableSize; y++) {
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

    public List<int[]> indexLine() {
        return Stream.of(hIndex, vIndex, lIndex, rIndex)
                .flatMap(Collection::stream)
                .toList();
    }

    public List<int[]> indexLine(int x, int y) {
        List<int[]> lines = new ArrayList<>();
        lines.add(hIndex.get(x));
        lines.add(vIndex.get(y));

        int arrIndex = x + y - 4;
        if (arrIndex >= 0 && arrIndex <= 20) lines.add(lIndex.get(arrIndex));

        arrIndex = x - y + (indexTableSize - 4 - 1);
        if (arrIndex >= 0 && arrIndex <= 20) lines.add(rIndex.get(arrIndex));

        return Collections.unmodifiableList(lines);
    }

    private void preStoreAllLines() {
        // horizontal
        for (int x = 0; x < indexTableSize; x++) {
            hIndex.add(indexTable[x]);
        }

        // vertical
        for (int y = 0; y < indexTableSize; y++) {
            int[] line = new int[indexTableSize];
            for (int x = 0; x < indexTableSize; x++) {
                line[x] = indexTable[x][y];
            }
            vIndex.add(line);
        }

        // left diagonal ( / direction, top left to bottom right)
        for (int start = 0; start < indexTableSize * 2 - 1; start++) {
            List<Integer> numList = new ArrayList<>();
            for (int x = 0; x < indexTableSize; x++) {
                int y = start - x;
                if (y >= 0 && y < indexTableSize) {
                    numList.add(indexTable[x][y]);
                }
            }
            if (numList.size() >= 5) {
                lIndex.add(numList.stream().mapToInt(i -> i).toArray());
            }
        }

        // right diagonal ( \ direction, bottom left to top right)
        for (int start = 0; start < indexTableSize * 2 - 1; start++) {
            List<Integer> numList = new ArrayList<>();
            for (int x = 0; x < indexTableSize; x++) {
                int y = start - (indexTableSize - 1 - x);
                if (y >= 0 && y < indexTableSize) {
                    numList.add(indexTable[x][y]);
                }
            }
            if (numList.size() >= 5) {
                rIndex.add(numList.stream().mapToInt(i -> i).toArray());
            }
        }
    }
}
