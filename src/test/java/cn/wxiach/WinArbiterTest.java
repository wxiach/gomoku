package cn.wxiach;

import cn.wxiach.core.rule.WinArbiter;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WinArbiterTest {

    private Board board;

    // Set up a new board before each test
    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    // Test case where there is no winner (no five in a row)
    @Test
    public void testCheckOverWithNoWinner() {
        // Reset the board to empty
        board.reset();

        // Simulate a board with no five in a row
        // Example: Place some stones at (7, 7) and (7, 8)
        board.set(Board.index(7, 7), Color.BLACK.value());
        board.set(Board.index(7, 8), Color.WHITE.value());

        // Assert that no five in a row exists
        assertFalse(WinArbiter.checkOver(board));
    }

    // Test case where there is a winner (five in a row)
    @Test
    public void testCheckOverWithWinner() {
        // Reset the board to empty
        board.reset();

        // Simulate a five-in-a-row situation
        // Example: Place five black stones from (7, 7) to (7, 11)
        for (int i = 0; i < 5; i++) {
            board.set(Board.index(7, 7 + i), Color.BLACK.value());
        }

        // Assert that five in a row is detected
        assertTrue(WinArbiter.checkOver(board));
    }

    // Test case where white wins with five in a row
    @Test
    public void testCheckOverWithWhiteWinner() {
        // Reset the board to empty
        board.reset();

        // Simulate a five-in-a-row situation for white
        // Example: Place five white stones from (7, 7) to (7, 11)
        for (int i = 0; i < 5; i++) {
            board.set(Board.index(7, 7 + i), Color.WHITE.value());
        }

        // Assert that white wins with five in a row
        assertTrue(WinArbiter.checkOver(board));
    }

    // Test case with five in a row and additional stones on the board
    @Test
    public void testCheckOverWithWinnerAndOtherStones() {
        // Reset the board to empty
        board.reset();

        // Simulate a five-in-a-row situation with additional stones
        // Place black stones at (7, 7) to (7, 11) to form a five-in-a-row
        for (int i = 0; i < 5; i++) {
            board.set(Board.index(7, 7 + i), Color.BLACK.value());
        }

        // Place some other stones on the board that are not part of the five-in-a-row
        board.set(Board.index(6, 6), Color.WHITE.value());
        board.set(Board.index(8, 8), Color.BLACK.value());

        // Assert that five in a row is detected despite the additional stones
        assertTrue(WinArbiter.checkOver(board));
    }

    // Test case with multiple stones but no five in a row
    @Test
    public void testCheckOverWithMultiplePoints() {
        // Reset the board to empty
        board.reset();

        // Place some stones but no five in a row
        board.set(Board.index(7, 7), Color.BLACK.value());
        board.set(Board.index(7, 8), Color.WHITE.value());
        board.set(Board.index(8, 7), Color.BLACK.value());

        // Assert that no five in a row exists
        assertFalse(WinArbiter.checkOver(board));
    }

    // Test case with an empty board and no moves made
    @Test
    public void testCheckOverWithInvalidBoard() {
        // Check if an empty board results in false
        board.reset();
        assertFalse(WinArbiter.checkOver(board));

        // Check if a board with no moves also results in false
        for (int i = 0; i < board.length(); i++) {
            board.set(i, Color.EMPTY.value());
        }
        assertFalse(WinArbiter.checkOver(board));
    }

    // Test case with edge positions (e.g., a five in a row on the edge)
    @Test
    public void testCheckOverWithEdgeCase() {
        // Reset the board to empty
        board.reset();

        // Simulate a five-in-a-row on the edge of the board
        // Example: Place black stones from (0, 0) to (0, 4)
        for (int i = 0; i < 5; i++) {
            board.set(Board.index(i, 0), Color.BLACK.value());
        }

        // Assert that five in a row on the edge is detected
        assertTrue(WinArbiter.checkOver(board));
    }

}


