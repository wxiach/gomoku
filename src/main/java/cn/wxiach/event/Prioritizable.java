package cn.wxiach.event;

/**
 * 为事件处理逻辑提供默认的优先级
 */
@FunctionalInterface
public interface Prioritizable {

    /**
     * 获取该订阅者的优先级
     *
     * @return 订阅者的优先级
     */
    SubscriberPriority defaultSubscriberPriority();
} 