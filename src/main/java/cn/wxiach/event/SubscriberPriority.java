package cn.wxiach.event;

/**
 * 为事件订阅者定义优先级。
 * 更高的整数值对应更高的优先级（首先执行）。
 */
public enum SubscriberPriority {
    LOGIC(100),

    UI(50), // Default priority

    EFFECT(0);

    private final int value;

    SubscriberPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
} 