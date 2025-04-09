package cn.wxiach.robot.search;

import cn.wxiach.core.model.Board;
import cn.wxiach.core.model.Color;
import cn.wxiach.core.model.Stone;

import java.util.Random;

/**
 * Generate a unique hash value for each possible board state in Gomoku.
 */
public class ZobristHash {
    private final long[][] table = new long[Board.SIZE * Board.SIZE][2];

    public ZobristHash() {
        Random random = new Random();
        for (int i = 0; i < table.length; i++) {
            table[i][0] = random.nextLong();
            table[i][1] = random.nextLong();
        }
    }

    public long compute(Board board) {
        long hash = 0;
        for (int i = 0; i < board.length(); i++) {
            if (board.color(i) == Color.EMPTY) continue;
            hash ^= table[i][board.color(i) == Color.BLACK ? 0 : 1];
        }
        return hash;
    }

    public long update(long hash, Stone stone) {
        return hash ^ table[Board.index(stone.point())][stone.color() == Color.BLACK ? 0 : 1];
    }
}
