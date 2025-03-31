package cn.wxiach.event.support;

import cn.wxiach.core.state.support.TurnStateReadable;
import cn.wxiach.event.GomokuEvent;

public class NewTurnEvent extends GomokuEvent {
    private final TurnStateReadable turn;

    public NewTurnEvent(Object source, TurnStateReadable turn) {
        super(source);
        this.turn = turn;
    }

    public TurnStateReadable getTurn() {
        return this.turn;
    }
}
