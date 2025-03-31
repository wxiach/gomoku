package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Color;

public class NewTurnEvent extends GomokuEvent {
    private final Color currentTurn;

    public NewTurnEvent(Object source, Color currentTurn) {
        super(source);
        this.currentTurn = currentTurn;
    }

    public Color getCurrentTurn() {
        return this.currentTurn;
    }
}
