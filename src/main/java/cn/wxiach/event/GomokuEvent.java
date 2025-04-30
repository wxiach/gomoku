package cn.wxiach.event;

import java.util.EventObject;

public abstract class GomokuEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public GomokuEvent(Object source) {
        super(source);
    }
}
