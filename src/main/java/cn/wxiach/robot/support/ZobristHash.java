package cn.wxiach.robot.support;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Stone;

import java.util.Random;


/**
 * Zobrist哈希算法的实现，用于为五子棋游戏中的每个棋盘状态生成唯一的哈希值。
 * <p>
 * Zobrist哈希是一种高效的增量哈希算法，常用于棋类游戏中进行棋盘状态的快速比较和查找。
 * 该实现使用了枚举单例模式，确保整个应用程序中使用相同的随机数表。
 * <p>
 * 算法原理：
 * 1. 为每个可能的棋子位置和颜色组合预先生成随机数
 * 2. 通过对所有已放置棋子对应的随机数进行异或操作，计算整个棋盘的哈希值
 * 3. 支持增量更新：放置或移除棋子时，只需对原哈希值与对应位置的随机数进行一次异或操作
 *
 * @author wxiach 2025/5/10
 */
public enum ZobristHash {
    INSTANCE;

    private final long[][] table = new long[Board.SIZE * Board.SIZE][2];

    ZobristHash() {
        Random random = new Random();
        for (int i = 0; i < table.length; i++) {
            table[i][0] = random.nextLong();
            table[i][1] = random.nextLong();
        }
    }

    public static long compute(Board board) {
        return INSTANCE.computeHash(board);
    }

    public static long update(long hash, Stone stone) {
        return INSTANCE.updateHash(hash, stone);
    }

    private long computeHash(Board board) {
        long hash = 0;
        for (int i = 0; i < board.length(); i++) {
            if (board.get(i) == Color.EMPTY.value()) continue;
            hash ^= table[i][board.get(i) == Color.BLACK.value() ? 0 : 1];
        }
        return hash;
    }

    private long updateHash(long hash, Stone stone) {
        return hash ^ table[Board.index(stone.point())][stone.color() == Color.BLACK ? 0 : 1];
    }
}
