package cn.wxiach.core.state;

import cn.wxiach.core.rule.BoardCheck;
import cn.wxiach.event.support.PiecePlacedEvent;
import cn.wxiach.model.EditableBoard;
import cn.wxiach.model.Piece;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.BoardUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BoardState extends PieceColorState {

    private static final Logger logger = LoggerFactory.getLogger(BoardState.class);

    private final EditableBoard board = new EditableBoard();

    public List<Piece> pieces() {
        return board.pieces();
    }

    public char[][] board() {
        return board.board();
    }

    public void addPiece(Piece piece) {
        if (BoardCheck.isOccupied(board.board(), piece.point())) return;
        board.addPiece(piece);
        logger.info("A {} piece is placed at ({}, {}).", piece.color().toString(), piece.point().x(), piece.point().y());
        GomokuEventBus.getInstance().publish(new BoardUpdateEvent(this, board));
        GomokuEventBus.getInstance().publish(new PiecePlacedEvent(this));
    }
}
