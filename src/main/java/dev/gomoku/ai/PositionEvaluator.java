package dev.gomoku.ai;

import dev.gomoku.model.Board;
import dev.gomoku.model.Direction;
import dev.gomoku.model.Stone;

/**
 * 以当前执子方为视角评估局面，并评估假设落子后的棋型，用于走法排序。
 * 棋型权重是相对启发分；确认能够形成连五的走法才使用 WIN_SCORE。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
final class PositionEvaluator {
    static final int WIN_SCORE = 10_000_000;
    private static final int MAX_HEURISTIC_SCORE = WIN_SCORE / 2;

    // 以下是相对棋型权重；活型两端可延伸，冲型只有一端可延伸。
    // FIVE_IN_A_ROW 是静态棋型分；搜索确认终局后使用更高的 WIN_SCORE。
    private static final int FIVE_IN_A_ROW = 500_000;
    private static final int OPEN_FOUR = 120_000;
    private static final int CLOSED_FOUR = 18_000;
    private static final int OPEN_THREE = 6_500;
    private static final int CLOSED_THREE = 450;
    private static final int OPEN_TWO = 300;
    private static final int CLOSED_TWO = 30;
    private static final int OPEN_ONE = 8;
    private static final int CLOSED_ONE = 2;

    // 下标为无对手棋子的五格窗口内己方棋子数，允许棋子之间存在空位。
    private static final int[] WINDOW_SCORES = {0, 2, 16, 180, 5_000, FIVE_IN_A_ROW};

    /** 综合连续棋型和允许断点的五格窗口评分。 */
    int evaluate(Board board, Stone player) {
        // 己方棋型加分、对方棋型减分，保证双方视角下的局面估值互为相反数。
        int score = 0;

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Stone stone = board.get(row, col);
                for (Direction direction : Direction.ALL) {
                    if (startsRun(board, row, col, stone, direction)) {
                        int run = scoreExistingRun(board, row, col, stone, direction);
                        score += stone == player ? run : -run;
                    }

                    // 连续棋段和五格窗口是两类叠加特征，窗口额外覆盖带空位的棋型。
                    score += scoreWindow(board, row, col, direction, player);
                }
            }
        }

        // 限制普通估值范围，避免将棋型优势误认为已经证明的胜负。
        return Math.clamp(score, -MAX_HEURISTIC_SCORE, MAX_HEURISTIC_SCORE);
    }

    /** 把目标空位视为己方棋子参与计算，不实际落子，也不改变棋盘哈希。 */
    int scoreMove(Board board, int row, int col, Stone player) {
        int score = 0;

        for (Direction direction : Direction.ALL) {
            int rowStep = direction.rowStep;
            int colStep = direction.colStep;

            // before/after 是目标两侧的连续棋子数，+1 计入尚未真正落下的目标棋子。
            int before = countStones(board, row, col, -rowStep, -colStep, player);
            int after = countStones(board, row, col, rowStep, colStep, player);
            int length = before + after + 1;
            if (length >= Board.WIN_LENGTH) {
                return WIN_SCORE;
            }

            // 越界或被对手占据都算封闭端；结果只能是 0、1 或 2。
            int openEnds = openEnd(board, row, col, -rowStep, -colStep, before + 1)
                    + openEnd(board, row, col, rowStep, colStep, after + 1);
            score += scoreRun(length, openEnds);
            score += scoreWindowsThroughMove(board, row, col, direction, player);
        }

        return score;
    }

    private boolean startsRun(Board board, int row, int col, Stone stone, Direction direction) {
        if (stone == Stone.EMPTY) {
            return false;
        }

        // 只从连续棋段的第一颗棋子计分，避免同一条连线被重复统计。
        int previousRow = row - direction.rowStep;
        int previousCol = col - direction.colStep;
        return !Board.inside(previousRow, previousCol) || board.get(previousRow, previousCol) != stone;
    }

    private int scoreExistingRun(Board board, int row, int col, Stone stone, Direction direction) {
        int length = 1 + countStones(board, row, col, direction.rowStep, direction.colStep, stone);
        int openEnds = openEnd(board, row, col, -direction.rowStep, -direction.colStep, 1)
                + openEnd(board, row, col, direction.rowStep, direction.colStep, length);
        return scoreRun(length, openEnds);
    }

    private int scoreWindow(Board board, int row, int col, Direction direction, Stone player) {
        int endRow = row + (Board.WIN_LENGTH - 1) * direction.rowStep;
        int endCol = col + (Board.WIN_LENGTH - 1) * direction.colStep;
        if (!Board.inside(endRow, endCol)) {
            return 0;
        }

        int ownCount = 0;
        int opponentCount = 0;
        Stone opponent = player.opposite();

        for (int offset = 0; offset < Board.WIN_LENGTH; offset++) {
            Stone cell = board.get(row + offset * direction.rowStep, col + offset * direction.colStep);
            if (cell == player) {
                ownCount++;
            } else if (cell == opponent) {
                opponentCount++;
            }
        }

        // 同时包含双方棋子的窗口无法形成同色连五，因此不计分。
        if (opponentCount == 0) {
            return WINDOW_SCORES[ownCount];
        }
        return ownCount == 0 ? -WINDOW_SCORES[opponentCount] : 0;
    }

    /** 检查所有包含目标位置的五格窗口，识别 XX.XX 这样的断点棋型。 */
    private int scoreWindowsThroughMove(Board board, int row, int col, Direction direction, Stone player) {
        int score = 0;

        // 相对目标位置，五格窗口的起点依次为 -4、-3、-2、-1、0。
        // 因而能覆盖所有包含目标的窗口，而不修改棋盘来模拟落子。
        for (int start = 1 - Board.WIN_LENGTH; start <= 0; start++) {
            int ownCount = 0;
            // 只有完整位于棋盘内、且不含对手棋子的窗口才有效。
            boolean uncontested = true;

            for (int offset = start; offset < start + Board.WIN_LENGTH; offset++) {
                int windowRow = row + offset * direction.rowStep;
                int windowCol = col + offset * direction.colStep;
                if (!Board.inside(windowRow, windowCol)) {
                    uncontested = false;
                    break;
                }

                Stone cell = board.get(windowRow, windowCol);
                if (cell != Stone.EMPTY && cell != player) {
                    uncontested = false;
                    break;
                }

                // offset 为零代表假设落子的目标，即使目前为空也计为己方棋子。
                if (offset == 0 || cell == player) {
                    ownCount++;
                }
            }

            if (uncontested) {
                score += WINDOW_SCORES[ownCount];
            }
        }

        return score;
    }

    private int scoreRun(int length, int openEnds) {
        if (length >= Board.WIN_LENGTH) {
            return FIVE_IN_A_ROW;
        }
        if (openEnds == 0) {
            return 0;
        }

        boolean bothEndsOpen = openEnds == 2;
        return switch (length) {
            case 4 -> bothEndsOpen ? OPEN_FOUR : CLOSED_FOUR;
            case 3 -> bothEndsOpen ? OPEN_THREE : CLOSED_THREE;
            case 2 -> bothEndsOpen ? OPEN_TWO : CLOSED_TWO;
            default -> bothEndsOpen ? OPEN_ONE : CLOSED_ONE;
        };
    }

    private int openEnd(Board board, int row, int col, int rowStep, int colStep, int distance) {
        return board.isEmpty(row + distance * rowStep, col + distance * colStep) ? 1 : 0;
    }

    private int countStones(Board board, int row, int col, int rowStep, int colStep, Stone stone) {
        int count = 0;

        for (int nextRow = row + rowStep, nextCol = col + colStep;
             Board.inside(nextRow, nextCol) && board.get(nextRow, nextCol) == stone;
             nextRow += rowStep, nextCol += colStep) {
            count++;
        }

        return count;
    }
}
