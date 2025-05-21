package cn.wxiach.event.types;

import cn.wxiach.core.store.state.TurnState;
import cn.wxiach.event.GomokuEvent;

public class NewTurnEvent extends GomokuEvent {

    private final TurnState turn;

    public NewTurnEvent(Object source, TurnState turn) {
        super(source);
        this.turn = turn;
    }

    public TurnState getTurn() {
        return turn;
    }
}
