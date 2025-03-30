package cn.wxiach.core.ai.search.support;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;

import java.util.Random;

public class ZobristHash {
    private final long[][][] table = new long[Board.SIZE][Board.SIZE][2];

    public ZobristHash() {
        Random random = new Random();
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                table[x][y][0] = random.nextLong();
                table[x][y][1] = random.nextLong();
            }
        }
    }

    public long compute(char[][] board) {
        long hash = 0;
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE; y++) {
                if (board[x][y] == Color.EMPTY.getValue()) continue;
                hash ^= table[x][y][board[x][y] == Color.BLACK.getValue() ? 0 : 1];
            }
        }
        return hash;
    }

    public long update(long hash, int x, int y, int piece) {
        return hash ^ table[x][y][piece == Color.BLACK.getValue() ? 0 : 1];
    }
}
