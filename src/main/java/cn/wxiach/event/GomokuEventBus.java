package cn.wxiach.event;

import javax.swing.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class GomokuEventBus {

    private static GomokuEventBus instance;

    private final ConcurrentHashMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = new ConcurrentHashMap<>();

    public static synchronized GomokuEventBus getInstance() {
        if (instance == null) {
            instance = new GomokuEventBus();
        }
        return instance;
    }

    public <T> void subscribe(Class<T> eventType, Subscriber<T> subscriber) {
        subscribers.computeIfAbsent(eventType, k->new CopyOnWriteArraySet<>()).add(subscriber);
    }

    public void publish(Object event) {
        if (SwingUtilities.isEventDispatchThread()) {
            dispatch(event);
        } else {
            SwingUtilities.invokeLater(() -> dispatch(event));
        }
    }

    public void dispatch(Object event) {
        CopyOnWriteArraySet<Subscriber> handlers = subscribers.get(event.getClass());
        if (handlers != null) {
            handlers.forEach(handler -> handler.handle(event));
        }
    }

    public interface Subscriber<T> {
        void handle(T event);
    }
}
