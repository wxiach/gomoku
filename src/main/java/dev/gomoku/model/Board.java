package dev.gomoku.model;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

/**
 * 保存可变棋盘和落子历史，不处理回合规则或界面交互。
 * Game 持有对弈棋盘；搜索只操作副本，每次试走都必须配对撤销。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
public final class Board {
    public static final int SIZE = 15;
    public static final int WIN_LENGTH = 5;
    public static final int CENTER = SIZE / 2;
    public static final int CELL_COUNT = SIZE * SIZE;

    // 使用固定随机种子，保证棋盘副本和不同测试运行采用一致的哈希键。
    private static final long HASH_SEED = 0x676f6d6f6b75L;
    // ZOBRIST[行][列][执子方]：该位置放置指定颜色棋子时对应的随机键。
    private static final long[][][] ZOBRIST = new long[SIZE][SIZE][Stone.PLAYERS.size()];
    // 同一棋子布局轮到白棋时额外异或的键，用来区分当前执子方。
    private static final long SIDE_KEY;

    static {
        SplittableRandom random = new SplittableRandom(HASH_SEED);
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                for (Stone player : Stone.PLAYERS) {
                    ZOBRIST[row][col][player.playerIndex()] = random.nextLong();
                }
            }
        }
        SIDE_KEY = random.nextLong();
    }

    private final Stone[][] cells = new Stone[SIZE][SIZE];
    private final ArrayList<Move> moves = new ArrayList<>();

    // 只包含棋子布局，不包含当前执子方；positionKey 再补充执子方信息。
    private long hash;

    public Board() {
        for (Stone[] row : cells) {
            java.util.Arrays.fill(row, Stone.EMPTY);
        }
    }

    /** 复制可变存储，同时保留落子历史和当前增量哈希，避免搜索与对弈共享棋盘。 */
    public Board copy() {
        Board copy = new Board();

        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(cells[row], 0, copy.cells[row], 0, SIZE);
        }

        // Move 是不可变记录，可以共享记录对象；列表和棋盘行数组必须独立。
        copy.moves.addAll(moves);
        copy.hash = hash;
        return copy;
    }

    /** 仅负责落子和更新棋盘；回合顺序及游戏结束后的限制由 Game 处理。 */
    public void place(int row, int col, Stone stone) {
        if (stone == null || stone == Stone.EMPTY || !isEmpty(row, col)) {
            throw new IllegalArgumentException("落子无效：位置必须在棋盘内且为空，棋子必须为黑棋或白棋");
        }

        cells[row][col] = stone;
        moves.add(new Move(row, col, stone));
        hash ^= ZOBRIST[row][col][stone.playerIndex()];
    }

    /** 只撤销最后一步；再次异或该步对应的键，即可恢复落子前的哈希。 */
    public Move undo() {
        if (moves.isEmpty()) {
            return null;
        }

        Move move = moves.removeLast();
        cells[move.row()][move.col()] = Stone.EMPTY;
        hash ^= ZOBRIST[move.row()][move.col()][move.stone().playerIndex()];
        return move;
    }

    public static boolean inside(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public Stone get(int row, int col) {
        return cells[row][col];
    }

    public boolean isEmpty(int row, int col) {
        return inside(row, col) && cells[row][col] == Stone.EMPTY;
    }

    public Move lastMove() {
        return moves.isEmpty() ? null : moves.getLast();
    }

    public int moveCount() {
        return moves.size();
    }

    public List<Move> moves() {
        return List.copyOf(moves);
    }

    public boolean isFull() {
        return moves.size() == CELL_COUNT;
    }

    /** 即使棋子布局相同，轮到不同执子方时也必须对应不同的置换表键。 */
    public long positionKey(Stone toMove) {
        return hash ^ (toMove == Stone.WHITE ? SIDE_KEY : 0);
    }

    public boolean winsAt(int row, int col) {
        if (!inside(row, col) || cells[row][col] == Stone.EMPTY) {
            return false;
        }

        // 目标棋子只计一次，再分别统计同一轴线两端的连续同色棋子。
        for (Direction direction : Direction.ALL) {
            if (1 + countConsecutive(row, col, direction.rowStep, direction.colStep)
                    + countConsecutive(row, col, -direction.rowStep, -direction.colStep) >= WIN_LENGTH) {
                return true;
            }
        }

        return false;
    }

    /** 返回经过最后一步的一条完整获胜连线；长连中的棋子也全部保留。 */
    public List<Move> winningLine() {
        Move last = lastMove();
        if (last == null) {
            return List.of();
        }

        for (Direction direction : Direction.ALL) {
            int before = countConsecutive(last.row(), last.col(), -direction.rowStep, -direction.colStep);
            int after = countConsecutive(last.row(), last.col(), direction.rowStep, direction.colStep);
            if (before + after + 1 >= WIN_LENGTH) {
                List<Move> line = new ArrayList<>();

                // 从反向最远端走到正向最远端，便于界面直接连接首尾坐标。
                for (int offset = -before; offset <= after; offset++) {
                    line.add(new Move(last.row() + offset * direction.rowStep,
                            last.col() + offset * direction.colStep, last.stone()));
                }

                return List.copyOf(line);
            }
        }

        return List.of();
    }

    private int countConsecutive(int row, int col, int rowStep, int colStep) {
        Stone stone = cells[row][col];
        int count = 0;

        for (int nextRow = row + rowStep, nextCol = col + colStep;
             inside(nextRow, nextCol) && cells[nextRow][nextCol] == stone;
             nextRow += rowStep, nextCol += colStep) {
            count++;
        }

        return count;
    }
}
