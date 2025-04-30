package cn.wxiach.event;

import java.util.function.Consumer;

/**
 * @author wxiach 2025/4/29
 */
public interface EventBusAware {

    default void publish(GomokuEvent event) {
        GomokuEventBus.getInstance().publish(event);
    }

    default <E extends GomokuEvent> void subscribe(Class<E> eventType, Consumer<? super E> subscriber) {
        GomokuEventBus.getInstance().subscribe(eventType, subscriber);
    }
}
