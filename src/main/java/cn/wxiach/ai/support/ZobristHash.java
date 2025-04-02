package cn.wxiach.ai.support;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;

import java.util.Random;

/**
 * Generate a unique hash value for each possible board state in Gomoku.
 */
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


    public long compute(Board board) {
        long hash = 0;
        for (Piece piece : board.pieces()) {
            hash ^= table[piece.x()][piece.y()][piece.color() == Color.BLACK ? 0 : 1];
        }
        return hash;
    }

    public long update(long hash, Piece piece) {
        return hash ^ table[piece.x()][piece.y()][piece.color() == Color.BLACK ? 0 : 1];
    }
}
