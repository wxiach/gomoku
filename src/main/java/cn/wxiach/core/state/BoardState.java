package cn.wxiach.core.state;

import cn.wxiach.config.GomokuConf;
import cn.wxiach.core.rule.*;
import cn.wxiach.event.support.PiecePlacedEvent;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.BoardUpdateEvent;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class BoardState extends PieceColorState {

    private final LinkedHashSet<Piece> pieces = new LinkedHashSet<>();
    private int[][] board = new int[GomokuConf.BOARD_SIZE][GomokuConf.BOARD_SIZE];

    public void setHumanColor(Color humanColor) {
        this.humanColor = humanColor;
        this.robotColor = humanColor == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public int[][] getBoard() {
        // Return a copy to protect the board not be modified.
        return deepCopyBoardArray(board);
    }

    public Piece getLastPiece() {
        return pieces.isEmpty() ? null : pieces.getLast();
    }

    public void addPiece(Piece piece) {
        if (PositionCheck.isOutOfBounds(piece.point().x(), piece.point().y())
                || !PositionCheck.isEmpty(board, piece.point())) return;
        pieces.add(piece);
        convertToArray();
        GomokuEventBus.getInstance().publish(new BoardUpdateEvent(this, board));
        GomokuEventBus.getInstance().publish(new PiecePlacedEvent(this));
    }

    private void convertToArray() {
        board = new int[GomokuConf.BOARD_SIZE][GomokuConf.BOARD_SIZE];
        pieces.forEach(piece -> {
            board[piece.point().x()][piece.point().y()] = piece.color().getValue();
        });
    }

    public static int[][] deepCopyBoardArray(int[][] board) {
        return Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
    }


}
