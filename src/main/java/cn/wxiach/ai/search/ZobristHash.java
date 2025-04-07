package cn.wxiach.ai.search;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Stone;

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
            char colorValue = board.get(i);
            if (colorValue == Color.EMPTY.value()) continue;
            hash ^= table[i][colorValue == Color.BLACK.value() ? 0 : 1];
        }
        return hash;
    }

    public long update(long hash, Stone stone) {
        return hash ^ table[Board.index(stone.point())][stone.color() == Color.BLACK ? 0 : 1];
    }
}
