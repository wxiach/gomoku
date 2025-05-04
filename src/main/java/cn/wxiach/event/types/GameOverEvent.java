package cn.wxiach.event.types;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Color;

public class GameOverEvent extends GomokuEvent {
    private final Color winner;

    public GameOverEvent(Object source, Color winner) {
        super(source);
        this.winner = winner;
    }

    public Color getWinner() {
        return winner;
    }
}
