package dev.gomoku.ai;

import dev.gomoku.model.Board;
import dev.gomoku.model.Move;
import dev.gomoku.model.Stone;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 验证搜索的必胜、防守、迭代加深、超时和中断行为。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
class AlphaBetaEngineTest {
    private SearchResult search(Board board, Stone side) {
        return new AlphaBetaEngine().findBestMove(board, side,
                new SearchConfig(3, Duration.ofSeconds(3), 12), null);
    }

    @ParameterizedTest
    @EnumSource(value = Stone.class, names = {"BLACK", "WHITE"})
    void eitherColorCanOpenAtCenter(Stone side) {
        SearchResult result = search(new Board(), side);
        assertEquals(new Move(7, 7, side), result.move());
    }

    @ParameterizedTest
    @EnumSource(value = Stone.class, names = {"BLACK", "WHITE"})
    void takesImmediateWinBeforeDefending(Stone side) {
        Board board = new Board();
        for (int col = 3; col <= 6; col++) {
            board.place(7, col, side);
            board.place(9, col, side.opposite());
        }
        SearchResult result = search(board, side);
        Move move = result.move();
        board.place(move.row(), move.col(), side);
        assertTrue(board.winsAt(move.row(), move.col()));
        assertEquals(1, result.depth());
    }

    @ParameterizedTest
    @EnumSource(value = Stone.class, names = {"BLACK", "WHITE"})
    void blocksForcedEdgeWinEvenWithCandidateLimitOne(Stone side) {
        Board board = new Board();
        for (int col = 0; col < 4; col++) {
            board.place(0, col, side.opposite());
        }
        SearchResult result = new AlphaBetaEngine().findBestMove(
                board, side, new SearchConfig(3, Duration.ofSeconds(3), 1), null);
        assertEquals(new Move(0, 4, side), result.move());
    }

    @Test
    void fillsBrokenFour() {
        Board board = new Board();
        for (int col : new int[]{4, 5, 7, 8}) {
            board.place(8, col, Stone.WHITE);
        }
        assertEquals(new Move(8, 6, Stone.WHITE), search(board, Stone.WHITE).move());
    }

    @Test
    void blocksDiagonalAtBoardBoundary() {
        Board board = new Board();
        for (int i = 0; i < 4; i++) {
            board.place(i, 14 - i, Stone.BLACK);
        }
        assertEquals(new Move(4, 10, Stone.WHITE), search(board, Stone.WHITE).move());
    }

    @Test
    void createsOpenFourFromOpenThree() {
        Board board = new Board();
        for (int col = 6; col <= 8; col++) {
            board.place(7, col, Stone.WHITE);
        }
        board.place(2, 2, Stone.BLACK);
        SearchResult result = search(board, Stone.WHITE);
        assertEquals(7, result.move().row());
        assertTrue(result.move().col() == 5 || result.move().col() == 9);
    }

    @Test
    void iterativeDeepeningPrunesAndPreservesSourceBoard() {
        Board board = midgame();
        long key = board.positionKey(Stone.WHITE);
        List<Move> moves = board.moves();
        List<Integer> depths = new ArrayList<>();
        SearchResult result = new AlphaBetaEngine().findBestMove(
                board, Stone.WHITE, new SearchConfig(4, Duration.ofSeconds(4), 10),
                update -> depths.add(update.depth()));
        assertNotNull(result.move());
        assertTrue(board.isEmpty(result.move().row(), result.move().col()));
        assertTrue(result.depth() >= 2);
        assertEquals(result.depth(), depths.size());
        assertTrue(result.nodes() > 0);
        assertTrue(result.cutoffs() > 0);
        assertTrue(result.tableHits() > 0);
        assertEquals(key, board.positionKey(Stone.WHITE));
        assertEquals(moves, board.moves());
    }

    @Test
    void expiredBudgetReturnsLegalFallbackPromptly() {
        Board board = midgame();
        SearchResult result = assertTimeoutPreemptively(Duration.ofSeconds(2),
                () -> new AlphaBetaEngine().findBestMove(
                        board, Stone.WHITE, new SearchConfig(32, Duration.ofNanos(1), 18), null));
        assertNotNull(result.move());
        assertTrue(board.isEmpty(result.move().row(), result.move().col()));
        assertEquals(0, result.depth());
    }

    @Test
    void interruptionKeepsLastCompletedIterationAndBoardIntact() {
        Board board = midgame();
        long key = board.positionKey(Stone.WHITE);
        try {
            SearchResult result = new AlphaBetaEngine().findBestMove(
                    board, Stone.WHITE, new SearchConfig(32, Duration.ofSeconds(30), 12),
                    update -> Thread.currentThread().interrupt());
            assertEquals(1, result.depth());
            assertEquals(key, board.positionKey(Stone.WHITE));
            assertTrue(board.isEmpty(result.move().row(), result.move().col()));
        } finally {
            Thread.interrupted();
        }
    }

    @Test
    void terminalAndFullBoardsReturnNoMove() {
        Board won = new Board();
        for (int col = 0; col < 5; col++) {
            won.place(0, col, Stone.BLACK);
        }
        assertNull(search(won, Stone.WHITE).move());
        Board full = new Board();
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                full.place(row, col, (row + 2 * col) % 4 < 2 ? Stone.BLACK : Stone.WHITE);
            }
        }
        assertNull(search(full, Stone.WHITE).move());
    }

    @Test
    void invalidSearchSettingsAreRejected() {
        AlphaBetaEngine engine = new AlphaBetaEngine();
        assertThrows(IllegalArgumentException.class,
                () -> engine.findBestMove(new Board(), Stone.EMPTY));
        assertThrows(IllegalArgumentException.class,
                () -> new SearchConfig(0, Duration.ofSeconds(1), 10));
        assertThrows(IllegalArgumentException.class,
                () -> new SearchConfig(3, Duration.ZERO, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new SearchConfig(3, Duration.ofSeconds(1), 0));
    }

    private static Board midgame() {
        Board board = new Board();
        board.place(7, 7, Stone.BLACK);
        board.place(7, 8, Stone.WHITE);
        board.place(8, 8, Stone.BLACK);
        board.place(6, 6, Stone.WHITE);
        board.place(8, 6, Stone.BLACK);
        board.place(6, 8, Stone.WHITE);
        board.place(9, 7, Stone.BLACK);
        return board;
    }
}
