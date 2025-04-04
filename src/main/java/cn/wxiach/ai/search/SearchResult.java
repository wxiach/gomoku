package cn.wxiach.ai.search;

import cn.wxiach.model.Board;
import cn.wxiach.model.Piece;

public record SearchResult(Piece piece, Board board, int score) {
    public SearchResult(Piece piece, Board board, int score) {
        this.piece = piece;
        this.board = board;
        this.score = score;
        board.addPiece(piece);
    }
}
