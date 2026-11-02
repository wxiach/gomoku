package dev.gomoku.ai;

import dev.gomoku.model.Board;
import dev.gomoku.model.Move;
import dev.gomoku.model.Stone;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 收集战术走法和邻近走法，并通过排序让 Alpha-Beta 尽早产生剪枝。
 * 排序历史仅属于本次搜索，由不同深度的迭代共同使用。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
final class MoveGenerator {
    private static final int NEIGHBOR_RADIUS = 2;
    private static final int NO_MOVE = -1;
    private static final int KILLERS_PER_PLY = 2;

    private static final int TABLE_MOVE_BONUS = 100_000_000;
    private static final int FIRST_KILLER_BONUS = 30_000;
    private static final int SECOND_KILLER_BONUS = 15_000;
    private static final int HISTORY_INCREMENT = 8;
    private static final int HISTORY_SCORE_LIMIT = 20_000;
    private static final int TACTICAL_WEIGHT = 2;

    private final PositionEvaluator evaluator;
    private final int candidateLimit;

    // killers[ply][0/1]：同一搜索层最近两次导致剪枝的位置编码。
    private final int[][] killers = new int[SearchConfig.MAX_SUPPORTED_DEPTH + 1][KILLERS_PER_PLY];
    // history[执子方][行 * 棋盘边长 + 列]：该位置在本次思考中累计的剪枝奖励。
    private final int[][] history = new int[Stone.PLAYERS.size()][Board.CELL_COUNT];

    MoveGenerator(PositionEvaluator evaluator, int candidateLimit) {
        this.evaluator = evaluator;
        this.candidateLimit = candidateLimit;

        for (int[] row : killers) {
            java.util.Arrays.fill(row, NO_MOVE);
        }
    }

    /**
     * 优先返回直接获胜的走法，其次返回必须封堵的位置，最后才是普通候选。
     * 调用方传入上一轮或置换表中的优先走法，以及用于检查超时和中断的回调。
     */
    List<Move> generate(Board board, Stone player, Move preferred, int ply, Runnable checkTime) {
        List<Candidate> normalMoves = new ArrayList<>();
        List<Candidate> winningMoves = new ArrayList<>();
        List<Candidate> blockingMoves = new ArrayList<>();

        for (int row = 0; row < Board.SIZE; row++) {
            checkTime.run();
            for (int col = 0; col < Board.SIZE; col++) {
                if (!board.isEmpty(row, col) || !hasNeighbor(board, row, col)) {
                    continue;
                }

                // 分别假设双方落在同一空位：进攻分衡量己方收益，防守分衡量对手威胁。
                int attack = evaluator.scoreMove(board, row, col, player);
                int defense = evaluator.scoreMove(board, row, col, player.opposite());
                Move move = new Move(row, col, player);
                Candidate candidate = new Candidate(move,
                        priority(move, preferred, ply, attack, defense));

                normalMoves.add(candidate);
                if (attack >= PositionEvaluator.WIN_SCORE) {
                    winningMoves.add(candidate);
                }
                if (defense >= PositionEvaluator.WIN_SCORE) {
                    blockingMoves.add(candidate);
                }
            }
        }

        // 能立即获胜就无需先防守；否则对手下一步能赢的位置必须优先封堵。
        // 若对手有多个获胜点，单步可能无法全部防住，递归搜索会继续判断结果。
        List<Candidate> selected = !winningMoves.isEmpty() ? winningMoves
                : !blockingMoves.isEmpty() ? blockingMoves : normalMoves;

        // 分数相同则按行列排序，使相同局面的搜索顺序可重复。
        selected.sort(Comparator.comparingInt(Candidate::priority).reversed()
                .thenComparingInt(candidate -> candidate.move.row())
                .thenComparingInt(candidate -> candidate.move.col()));

        // 强制走法全部保留，只有普通候选按数量截断。
        boolean forced = !winningMoves.isEmpty() || !blockingMoves.isEmpty();
        int limit = forced ? selected.size() : Math.min(candidateLimit, selected.size());
        return selected.subList(0, limit).stream().map(Candidate::move).toList();
    }

    void recordCutoff(Stone player, Move move, int depth, int ply) {
        int index = cellIndex(move);

        // 保留同一搜索层最近两次导致剪枝的走法。
        if (killers[ply][0] != index) {
            killers[ply][1] = killers[ply][0];
            killers[ply][0] = index;
        }

        // 较深搜索中的剪枝获得更高历史分；设置上限防止分数无限累加。
        int[] playerHistory = history[player.playerIndex()];
        playerHistory[index] = Math.min(HISTORY_SCORE_LIMIT,
                playerHistory[index] + depth * depth * HISTORY_INCREMENT);
    }

    private boolean hasNeighbor(Board board, int row, int col) {
        int firstRow = Math.max(0, row - NEIGHBOR_RADIUS);
        int lastRow = Math.min(Board.SIZE - 1, row + NEIGHBOR_RADIUS);
        int firstCol = Math.max(0, col - NEIGHBOR_RADIUS);
        int lastCol = Math.min(Board.SIZE - 1, col + NEIGHBOR_RADIUS);

        for (int neighborRow = firstRow; neighborRow <= lastRow; neighborRow++) {
            for (int neighborCol = firstCol; neighborCol <= lastCol; neighborCol++) {
                if (board.get(neighborRow, neighborCol) != Stone.EMPTY) {
                    return true;
                }
            }
        }

        return false;
    }

    private int priority(Move move, Move preferred, int ply, int attack, int defense) {
        int index = cellIndex(move);
        // 到中心的曼哈顿距离只用于细微排序，不应压过战术分和缓存推荐。
        int centerDistance = Math.abs(move.row() - Board.CENTER) + Math.abs(move.col() - Board.CENTER);
        int priority = (attack + defense) * TACTICAL_WEIGHT
                + history[move.stone().playerIndex()][index] - centerDistance;

        if (move.equals(preferred)) {
            priority += TABLE_MOVE_BONUS;
        }

        if (killers[ply][0] == index) {
            priority += FIRST_KILLER_BONUS;
        } else if (killers[ply][1] == index) {
            priority += SECOND_KILLER_BONUS;
        }

        return priority;
    }

    /** 将二维行列压成一维数组下标；行列本身仍从零开始。 */
    private int cellIndex(Move move) {
        return move.row() * Board.SIZE + move.col();
    }

    /**
     * 候选走法及其搜索顺序，排序优先级不能代替递归搜索得到的局面评分。
     *
     * @param move 候选的落子位置和执子颜色
     * @param priority 越大越优先搜索，仅用于走法排序
     * @author wxiach@gmail.com 2026/11/02
     */
    private record Candidate(Move move, int priority) {
    }
}
