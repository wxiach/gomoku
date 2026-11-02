package dev.gomoku.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 验证游戏回合、终局、和棋以及双方执子时的悔棋行为。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
class GameTest {
    @Test
    void blackStartsAndInvalidClicksDoNotConsumeTurns() {
        Game game = new Game();
        assertEquals(Stone.BLACK, game.turn());
        assertFalse(game.play(-1, 0));
        assertEquals(Stone.BLACK, game.turn());
        assertTrue(game.play(7, 7));
        assertEquals(Stone.WHITE, game.turn());
        assertFalse(game.play(7, 7));
        assertEquals(Stone.WHITE, game.turn());
    }

    @Test
    void winStopsPlayAndUndoReopensGame() {
        Game game = new Game();
        for (int col = 0; col < 4; col++) {
            game.play(0, col);
            game.play(2, col);
        }
        assertTrue(game.play(0, 4));
        assertTrue(game.isOver());
        assertEquals(Stone.BLACK, game.winner());
        assertFalse(game.play(5, 5));
        assertTrue(game.undoHumanTurn(Stone.BLACK));
        assertEquals(8, game.board().moveCount());
        assertEquals(Stone.BLACK, game.turn());
        assertFalse(game.isOver());
        assertEquals(Stone.EMPTY, game.winner());
    }

    @Test
    void undoAfterAiResponseRemovesOneHumanTurn() {
        Game game = new Game();
        game.play(7, 7);
        game.play(7, 8);
        assertTrue(game.undoHumanTurn(Stone.BLACK));
        assertEquals(0, game.board().moveCount());
        assertEquals(Stone.BLACK, game.turn());
    }

    @Test
    void whiteCanUndoWithoutRemovingAiOpening() {
        Game game = new Game();
        game.play(7, 7);
        assertFalse(game.canUndo(Stone.WHITE));
        assertFalse(game.undoHumanTurn(Stone.WHITE));
        game.play(7, 8);
        game.play(8, 7);
        assertTrue(game.undoHumanTurn(Stone.WHITE));
        assertEquals(1, game.board().moveCount());
        assertEquals(Stone.BLACK, game.board().get(7, 7));
        assertEquals(Stone.WHITE, game.turn());
    }

    @Test
    void undoDuringAiTurnRemovesOnlyPendingHumanMove() {
        Game game = new Game();
        game.play(7, 7);
        game.play(7, 8);
        game.play(8, 7);
        assertTrue(game.undoHumanTurn(Stone.BLACK));
        assertEquals(2, game.board().moveCount());
        assertEquals(Stone.BLACK, game.turn());
    }

    @Test
    void fullBoardWithoutFiveIsADraw() {
        Game game = new Game();
        List<int[]> black = new ArrayList<>();
        List<int[]> white = new ArrayList<>();
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                ((row + 2 * col) % 4 < 2 ? black : white).add(new int[]{row, col});
            }
        }
        int blackIndex = 0;
        int whiteIndex = 0;
        for (int i = 0; i < Board.SIZE * Board.SIZE; i++) {
            int[] position = game.turn() == Stone.BLACK ? black.get(blackIndex++) : white.get(whiteIndex++);
            assertTrue(game.play(position[0], position[1]));
        }
        assertTrue(game.isOver());
        assertTrue(game.board().isFull());
        assertEquals(Stone.EMPTY, game.winner());
        game.reset();
        assertEquals(Stone.BLACK, game.turn());
        assertEquals(0, game.board().moveCount());
        assertFalse(game.isOver());
    }
}
