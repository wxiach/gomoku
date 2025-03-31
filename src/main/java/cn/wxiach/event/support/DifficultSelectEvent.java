package cn.wxiach.event.support;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Difficult;

public class DifficultSelectEvent extends GomokuEvent {
    private final Difficult difficult;

    public DifficultSelectEvent(Object source, Difficult difficult) {
        super(source);
        this.difficult = difficult;
    }

    public Difficult getDifficult() {
        return difficult;
    }
}
