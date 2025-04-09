package cn.wxiach.robot.pattern;

import cn.wxiach.core.model.Board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class BoardIndexTable {

    private final static int size = Board.SIZE;

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
                .filter(line -> line.length >= 5)
                .toList();
    }

    public List<int[]> indexLine(int x, int y) {
        return Stream.of(
                        hIndex.get(y),
                        vIndex.get(x),
                        lIndex.get(x + y),
                        rIndex.get(x - y + size - 1)
                )
                .filter(line -> line.length >= 5)
                .toList();

    }

    private void preStoreAllLines() {
        // horizontal
        for (int y = 0; y < size; y++) {
            int[] line = new int[size];
            for (int x = 0; x < size; x++) {
                line[x] = y * size + x;
            }
            hIndex.add(line);
        }

        // vertical
        for (int x = 0; x < size; x++) {
            int[] line = new int[size];
            for (int y = 0; y < size; y++) {
                line[y] = y * size + x;
            }
            vIndex.add(line);
        }

        // left diagonal ( / direction, top left to bottom right)
        // The characteristic of the forward slash (/) is that x + y remains constant.
        for (int start = 0; start <= (size - 1) * 2; start++) {
            List<Integer> numList = new ArrayList<>();
            for (int x = 0; x < size; x++) {
                int y = start - x;
                if (y >= 0 && y < size) {
                    numList.add(y * size + x);
                }
            }
            lIndex.add(numList.stream().mapToInt(i -> i).toArray());
        }

        // right diagonal ( \ direction, bottom left to top right)
        // The characteristic of the backslash (\) is that x - y remains constant.
        for (int start = -(size - 1); start <= size - 1; start++) {
            List<Integer> numList = new ArrayList<>();
            for (int x = 0; x < size; x++) {
                int y = x - start;
                if (y >= 0 && y < size) {
                    numList.add(y * size + x);
                }
            }
            rIndex.add(numList.stream().mapToInt(i -> i).toArray());
        }
    }
}

