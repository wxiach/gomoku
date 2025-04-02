package cn.wxiach.ai.search;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;

import java.util.Collection;

public record SearchContext(Board board, Color color, int depth) {
    public Collection<Piece> boardPieces() {
        return board.pieces();
    }

    public char[][] boardMatrix() {
        return board.matrix();
    }
}
