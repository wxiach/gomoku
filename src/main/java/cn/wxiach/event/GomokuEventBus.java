package cn.wxiach.event;

import javax.swing.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

public class GomokuEventBus {

    private static GomokuEventBus instance;

    private final ConcurrentHashMap<Class<? extends GomokuEvent>, CopyOnWriteArraySet<Consumer<? super GomokuEvent>>> subscribers = new ConcurrentHashMap<>();

    public static synchronized GomokuEventBus getInstance() {
        if (instance == null) {
            instance = new GomokuEventBus();
        }
        return instance;
    }

    public <E extends GomokuEvent> void subscribe(Class<E> eventType, Consumer<? super E> subscriber) {
        subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArraySet<>())
                .add(createTypeSafeConsumer(eventType, subscriber));
    }

    public void publish(GomokuEvent event) {
        if (SwingUtilities.isEventDispatchThread()) {
            dispatch(event);
        } else {
            SwingUtilities.invokeLater(() -> dispatch(event));
        }
    }

    private void dispatch(GomokuEvent event) {
        Class<? extends GomokuEvent> eventType = event.getClass();
        CopyOnWriteArraySet<Consumer<? super GomokuEvent>> handlers = subscribers.get(eventType);
        if (handlers != null) {
            handlers.forEach(handler -> handler.accept(event));
        }
    }

    private <E extends GomokuEvent> Consumer<GomokuEvent> createTypeSafeConsumer(
            Class<E> eventType, Consumer<? super E> subscriber) {

        Class<?> parameterType = subscriber.getClass().getDeclaredMethods()[0].getParameterTypes()[0];
        return event -> {
            if (parameterType.isAssignableFrom(eventType)) {
                subscriber.accept(eventType.cast(event));
            } else {
                throw new IllegalArgumentException(
                        String.format("Subscriber for %s cannot handle %s.", eventType.getName(), parameterType.getName())
                );
            }
        };
    }
}
