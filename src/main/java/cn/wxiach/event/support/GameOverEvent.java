package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Piece;

public class GameOverEvent extends GomokuEvent {
    private final Piece.Color winner;

    public GameOverEvent(Object source, Piece.Color winner) {
        super(source);
        this.winner = winner;
    }

    public Piece.Color getWinner() {
        return winner;
    }
}
