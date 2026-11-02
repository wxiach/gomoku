package dev.gomoku.ai;

import dev.gomoku.model.Board;
import dev.gomoku.model.Stone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 验证棋型评分的双方对称性、开放端权重和假设落子的无副作用约定。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
class PositionEvaluatorTest {
    private final PositionEvaluator evaluator = new PositionEvaluator();

    @Test
    void evaluationIsSymmetricBetweenPlayers() {
        Board board = new Board();
        board.place(7, 7, Stone.BLACK);
        board.place(7, 8, Stone.WHITE);
        board.place(8, 7, Stone.BLACK);
        assertEquals(evaluator.evaluate(board, Stone.BLACK), -evaluator.evaluate(board, Stone.WHITE));
    }

    @Test
    void openFourOutranksClosedFour() {
        Board open = new Board();
        for (int col = 4; col <= 6; col++) {
            open.place(7, col, Stone.BLACK);
        }
        Board closed = open.copy();
        closed.place(7, 3, Stone.WHITE);
        assertTrue(evaluator.scoreMove(open, 7, 7, Stone.BLACK)
                > evaluator.scoreMove(closed, 7, 7, Stone.BLACK));
    }

    @Test
    void completingBrokenFiveHasTerminalPriorityWithoutPlacingStone() {
        Board board = new Board();
        for (int col : new int[]{4, 5, 7, 8}) {
            board.place(7, col, Stone.WHITE);
        }
        long key = board.positionKey(Stone.WHITE);
        assertEquals(PositionEvaluator.WIN_SCORE, evaluator.scoreMove(board, 7, 6, Stone.WHITE));
        assertTrue(board.isEmpty(7, 6));
        assertEquals(key, board.positionKey(Stone.WHITE));
    }
}
