package cn.wxiach.event.support;

import cn.wxiach.core.model.Color;
import cn.wxiach.event.GomokuEvent;

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
