package cn.wxiach.event;

import java.util.EventObject;

public abstract class GomokuEvent extends EventObject {

    /**
     * 构造一个原型事件。
     *
     * @param source 事件最初发生的对象
     * @throws IllegalArgumentException 如果source为null
     */
    public GomokuEvent(Object source) {
        super(source);
    }
}
