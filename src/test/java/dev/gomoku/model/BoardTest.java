package dev.gomoku.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 验证棋盘的连线判定、合法落子、复制、撤销和哈希恢复。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
class BoardTest {
    @ParameterizedTest
    @CsvSource({"0,1", "1,0", "1,1", "1,-1"})
    void recognizesAllFourDirections(int dr, int dc) {
        Board board = new Board();
        for (int i = 0; i < 5; i++) {
            board.place(4 + i * dr, 7 + i * dc, Stone.BLACK);
        }
        Move last = board.lastMove();
        assertTrue(board.winsAt(last.row(), last.col()));
        assertEquals(5, board.winningLine().size());
    }

    @Test
    void overlineWinsUnderFreestyleRules() {
        Board board = new Board();
        for (int col : new int[]{0, 1, 2, 4, 5, 3}) {
            board.place(0, col, Stone.WHITE);
        }
        assertTrue(board.winsAt(0, 3));
        assertEquals(6, board.winningLine().size());
    }

    @Test
    void gapsAndOpposingStonesBreakLines() {
        Board board = new Board();
        for (int col : new int[]{0, 1, 3, 4}) {
            board.place(0, col, Stone.BLACK);
        }
        assertFalse(board.winsAt(0, 4));
        board.place(0, 2, Stone.WHITE);
        assertFalse(board.winsAt(0, 0));
        assertTrue(board.winningLine().isEmpty());
        assertFalse(board.winsAt(-1, 0));
        assertFalse(board.winsAt(2, 2));
    }

    @Test
    void rejectsInvalidMovesWithoutChangingPosition() {
        Board board = new Board();
        board.place(7, 7, Stone.BLACK);
        long key = board.positionKey(Stone.WHITE);
        assertThrows(IllegalArgumentException.class, () -> board.place(7, 7, Stone.WHITE));
        assertThrows(IllegalArgumentException.class, () -> board.place(-1, 7, Stone.BLACK));
        assertThrows(IllegalArgumentException.class, () -> board.place(7, 8, Stone.EMPTY));
        assertThrows(IllegalArgumentException.class, () -> board.place(7, 8, null));
        assertEquals(1, board.moveCount());
        assertEquals(key, board.positionKey(Stone.WHITE));
    }

    @Test
    void copyIsIndependentAndUndoRestoresHash() {
        Board board = new Board();
        long emptyKey = board.positionKey(Stone.BLACK);
        board.place(7, 7, Stone.BLACK);
        long occupiedKey = board.positionKey(Stone.WHITE);
        assertNotEquals(board.positionKey(Stone.BLACK), occupiedKey);
        Board copy = board.copy();
        assertEquals(occupiedKey, copy.positionKey(Stone.WHITE));
        copy.place(7, 8, Stone.WHITE);
        assertTrue(board.isEmpty(7, 8));
        copy.undo();
        assertEquals(occupiedKey, copy.positionKey(Stone.WHITE));
        board.undo();
        assertEquals(emptyKey, board.positionKey(Stone.BLACK));
        assertNull(board.undo());
    }

    @Test
    void transpositionsHaveTheSameKey() {
        Board first = new Board();
        first.place(7, 7, Stone.BLACK);
        first.place(7, 8, Stone.WHITE);
        Board second = new Board();
        second.place(7, 8, Stone.WHITE);
        second.place(7, 7, Stone.BLACK);
        assertEquals(first.positionKey(Stone.BLACK), second.positionKey(Stone.BLACK));
    }
}
