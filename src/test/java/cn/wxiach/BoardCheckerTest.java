package cn.wxiach;

import cn.wxiach.core.model.Board;
import cn.wxiach.core.model.Color;
import cn.wxiach.core.model.Point;
import cn.wxiach.core.model.Stone;
import cn.wxiach.core.rule.BoardChecker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardCheckerTest {

    @Test
    public void testIsOnBoard() {
        // Test points on the boundaries of the board
        assertTrue(BoardChecker.isOnBoard(Point.of(0, 0)));  // Top-left corner
        assertTrue(BoardChecker.isOnBoard(Point.of(Board.SIZE - 1, Board.SIZE - 1)));  // Bottom-right corner
        assertTrue(BoardChecker.isOnBoard(Point.of(Board.SIZE - 1, 0)));  // Bottom-left corner
        assertTrue(BoardChecker.isOnBoard(Point.of(0, Board.SIZE - 1)));  // Top-right corner

        // Test points outside the boundaries
        assertFalse(BoardChecker.isOnBoard(Point.of(-1, 0)));  // Outside top boundary
        assertFalse(BoardChecker.isOnBoard(Point.of(0, -1)));  // Outside left boundary
        assertFalse(BoardChecker.isOnBoard(Point.of(Board.SIZE, Board.SIZE)));  // Outside bottom-right corner
        assertFalse(BoardChecker.isOnBoard(Point.of(Board.SIZE - 1, Board.SIZE)));  // Outside top boundary
    }

    @Test
    public void testIsEmpty() {
        Board board = new Board();  // Create a 15x15 empty board
        Point emptyPoint = Point.of(5, 5);  // A point that is initially empty

        // By default, the board is empty
        assertTrue(BoardChecker.isEmpty(board, emptyPoint));

        // Set the point as occupied by placing a stone
        Stone stone = Stone.of(emptyPoint, Color.BLACK);  // Assuming Stone is a class representing a placed stone
        board.makeMove(stone);

        // Check that the point is no longer empty
        assertFalse(BoardChecker.isEmpty(board, emptyPoint));
    }

    @Test
    public void testIsOccupied() {
        Board board = new Board();  // Create a 15x15 empty board
        Point emptyPoint = Point.of(5, 5);  // A point that is initially empty

        // By default, the board is empty
        assertFalse(BoardChecker.isOccupied(board, emptyPoint));

        // Set the point as occupied by placing a stone
        Stone stone = Stone.of(emptyPoint, Color.BLACK);  // Assuming Stone is a class representing a placed stone
        board.makeMove(stone);

        // Check that the point is now occupied
        assertTrue(BoardChecker.isOccupied(board, emptyPoint));
    }
}

