package cn.wxiach;

import cn.wxiach.core.model.Board;
import cn.wxiach.core.model.Color;
import cn.wxiach.core.model.Stone;
import cn.wxiach.robot.search.BoardWithZobrist;
import cn.wxiach.robot.search.ZobristHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BoardWithZobristTest {

    private BoardWithZobrist boardWithZobrist;
    private ZobristHash zobristHash = new ZobristHash();
    private Board board;

    @BeforeEach
    public void setUp() {
        // Set up the board and zobrist hash instance
        board = new Board(); // Using the default constructor for the board
        boardWithZobrist = new BoardWithZobrist(board, zobristHash); // Create the BoardWithZobrist instance
    }

    @Test
    public void testInitialHash() {
        // Test that the initial hash is not zero (since empty board hash is set to 0)
        long initialHash = boardWithZobrist.hash();
        assertEquals(0, initialHash, "Initial hash should be 0 for an empty board.");
    }

    @Test
    public void testMakeMoveUpdatesHash() {
        // Test that making a move updates the hash
        Stone stone = Stone.of(7, 7, Color.BLACK); // Create a black stone
        long initialHash = boardWithZobrist.hash();

        // Make a move with the black stone
        boardWithZobrist.makeMove(stone);

        // Ensure the hash changes after the move
        assertNotEquals(initialHash, boardWithZobrist.hash(), "Hash should change after making a move.");
    }

    @Test
    public void testUndoMoveUpdatesHash() {
        // Test that undoing a move updates the hash correctly
        Stone stone = Stone.of(7, 7, Color.BLACK); // Create a black stone

        boardWithZobrist.makeMove(stone);
        long hashAfterMove = boardWithZobrist.hash();

        // Undo the move
        boardWithZobrist.undoMove(stone);

        // Ensure the hash returns to its value before the move
        assertEquals(0, boardWithZobrist.hash(), "Hash should return to the previous value after undo.");
    }
}

