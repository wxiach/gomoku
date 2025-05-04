package cn.wxiach.event;

/**
 * Interface indicating that a class can provide a default priority
 * for its event handling logic.
 */
@FunctionalInterface
public interface Prioritizable {

    /**
     * Gets the default priority level for this subscriber.
     *
     * @return The default SubscriberPriority.
     */
    SubscriberPriority defaultSubscriberPriority();
} 