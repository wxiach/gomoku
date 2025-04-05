package cn.wxiach;

import cn.wxiach.ai.support.BoardIndexTable;
import cn.wxiach.model.Board;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardIndexTableTest {

    private static final int BOARD_SIZE = Board.SIZE;

    @Test
    void testPreStoreAllLines() {
        BoardIndexTable table = new BoardIndexTable();

        List<int[]> lines = table.indexLine();

        // Verify total lines count
        int expectedLinesCount = BOARD_SIZE + BOARD_SIZE + (BOARD_SIZE * 2 - 1) * 2;
        assertEquals(expectedLinesCount, lines.size());

        // Verify horizontal line length
        for (int i = 0; i < BOARD_SIZE; i++) {
            assertEquals(BOARD_SIZE, table.indexLine(i, 0).get(0).length);
        }

        // Verify vertical line length
        for (int i = 0; i < BOARD_SIZE; i++) {
            assertEquals(BOARD_SIZE, table.indexLine(0, i).get(1).length);
        }

        // Verify diagonal lines
        assertTrue(table.indexLine(0, 0).stream().anyMatch(line -> line.length >= 5));
        assertTrue(table.indexLine(BOARD_SIZE - 1, BOARD_SIZE - 1).stream().anyMatch(line -> line.length >= 5));
    }

    @Test
    void testIndexLineAtSpecificCoordinate() {
        BoardIndexTable table = new BoardIndexTable();

        // Test at center point
        int center = BOARD_SIZE / 2;
        List<int[]> linesAtCenter = table.indexLine(center, center);
        assertEquals(4, linesAtCenter.size());
        for (int[] line : linesAtCenter) {
            assertTrue(line.length >= 5);
        }

        // Test at corner point
        List<int[]> linesAtCorner = table.indexLine(0, 0);
        for (int[] line : linesAtCorner) {
            assertTrue(line.length >= 5);
        }

        // Test edge case
        List<int[]> linesAtEdge = table.indexLine(0, BOARD_SIZE - 1);
        for (int[] line : linesAtEdge) {
            assertTrue(line.length >= 5);
        }
    }

    @Test
    void testSpecificLineValues() {
        BoardIndexTable table = new BoardIndexTable();

        // Check specific horizontal line (row 0)
        int[] expectedHorizontal = new int[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            expectedHorizontal[i] = i;
        }
        assertArrayEquals(expectedHorizontal, table.indexLine(0, 0).get(0));

        // Check specific vertical line (column 0)
        int[] expectedVertical = new int[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            expectedVertical[i] = i * BOARD_SIZE;
        }
        assertArrayEquals(expectedVertical, table.indexLine(0, 0).get(1));

        // Check specific diagonal line (top-left to bottom-right)
        int[] expectedDiagonal = new int[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            expectedDiagonal[i] = i * (BOARD_SIZE + 1);
        }
        List<int[]> lines = table.indexLine(0, 0);
        int[] actualDiagonal = lines.stream()
                .filter(line -> line.length == BOARD_SIZE && line[0] == 0 && line[1] == BOARD_SIZE + 1)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected diagonal line not found"));

        assertArrayEquals(expectedDiagonal, actualDiagonal);


    }
}
