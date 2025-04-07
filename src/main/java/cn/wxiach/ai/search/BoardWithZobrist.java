package cn.wxiach.ai.search;

import cn.wxiach.model.Board;
import cn.wxiach.model.Stone;

public class BoardWithZobrist extends Board {

    private final ZobristHash zobristHash;
    private long hash;

    public BoardWithZobrist(Board board, ZobristHash zobristHash) {
        super(board.vector());
        this.zobristHash = zobristHash;
        this.hash = zobristHash.compute(this);
    }

    public long hash() {
        return hash;
    }

    @Override
    public void makeMove(Stone stone) {
        super.makeMove(stone);
        this.hash = zobristHash.update(hash, stone);
    }

    @Override
    public void undoMove(Stone stone) {
        super.undoMove(stone);
        this.hash = zobristHash.update(hash, stone);
    }
}
