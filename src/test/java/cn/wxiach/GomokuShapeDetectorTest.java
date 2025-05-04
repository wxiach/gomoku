package cn.wxiach;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.robot.features.GomokuShapeDetector;
import cn.wxiach.robot.features.Pattern;
import cn.wxiach.robot.features.PatternCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GomokuShapeDetectorTest {
    private Board board;
    private GomokuShapeDetector detector;

    /**
     * Helper method to set up the board with predefined values
     */
    private Board setupBoard() {
        Board board = new Board();
        board.set(Board.index(7, 7), Color.BLACK.value());
        board.set(Board.index(7, 6), Color.WHITE.value());
        board.set(Board.index(6, 6), Color.BLACK.value());
        board.set(Board.index(5, 5), Color.WHITE.value());
        board.set(Board.index(6, 7), Color.BLACK.value());
        board.set(Board.index(6, 5), Color.WHITE.value());
        board.set(Board.index(8, 7), Color.BLACK.value());
        board.set(Board.index(5, 7), Color.WHITE.value());
        board.set(Board.index(10, 7), Color.BLACK.value());
        board.set(Board.index(5, 6), Color.WHITE.value());

        return board;
    }

    @BeforeEach
    public void setUp() {
        // Initialize the board and detector before each test
        board = setupBoard();
        detector = GomokuShapeDetector.getInstance();
    }

    @Test
    public void testDetectWithBoard() {
        // Call the detect method to find patterns
        Collection<Pattern> patterns = detector.detect(board);

        // Verify that patterns are detected and not empty
        assertNotNull(patterns);
        assertFalse(patterns.isEmpty());
        assertEquals(7, patterns.size());

        assertTrue(patterns.stream().anyMatch(pattern -> pattern.name().equals(PatternCollection.D4)));
    }

    @Test
    public void testDetectWithBoardAndPoint() {
        Collection<Pattern> patterns = detector.detect(board, Point.of(8, 7));

        // Verify that patterns are detected and not empty
        assertNotNull(patterns);
        assertFalse(patterns.isEmpty());
        assertEquals(3, patterns.size());
        assertTrue(patterns.stream().anyMatch(pattern -> pattern.name().equals(PatternCollection.D4)));
    }

    @Test
    public void testEmptyBoard() {
        Board emptyBoard = new Board();
        Collection<Pattern> patterns = detector.detect(emptyBoard);
        assertTrue(patterns.isEmpty());
    }
}


