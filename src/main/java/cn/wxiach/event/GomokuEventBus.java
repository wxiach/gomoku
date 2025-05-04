package cn.wxiach.event;

import cn.wxiach.utils.Log;

import javax.swing.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

public class GomokuEventBus {

    private GomokuEventBus() {
    }

    private static class Holder {
        private static final GomokuEventBus INSTANCE = new GomokuEventBus();
    }

    public static GomokuEventBus getInstance() {
        return Holder.INSTANCE;
    }

    private final ConcurrentHashMap<
            Class<? extends GomokuEvent>,
            ConcurrentSkipListMap<Integer, CopyOnWriteArraySet<Consumer<? super GomokuEvent>>>
            > subscribers = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <E extends GomokuEvent> void subscribe(
            Class<E> eventType,
            Consumer<? super E> subscriber,
            SubscriberPriority priority
    ) {
        addSubscriber(eventType, (Consumer<? super GomokuEvent>) subscriber, priority.getValue());
    }

    private void addSubscriber(Class<? extends GomokuEvent> type, Consumer<? super GomokuEvent> consumer, int priority) {
        subscribers
                .computeIfAbsent(type, k -> new ConcurrentSkipListMap<>())
                .computeIfAbsent(priority, k -> new CopyOnWriteArraySet<>())
                .add(consumer);
    }

    public void publish(GomokuEvent event) {
        if (SwingUtilities.isEventDispatchThread()) {
            dispatch(event);
        } else {
            SwingUtilities.invokeLater(() -> dispatch(event));
        }
    }

    private void dispatch(GomokuEvent event) {
        var priorityMap = subscribers.get(event.getClass());
        if (priorityMap == null) return;

        for (var handlers : priorityMap.descendingMap().values()) {
            for (var handler : handlers) {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    Log.error("Exception during event dispatch for {} to handler {}",
                            event.getClass().getName(),
                            handler.getClass().getName());
                }
            }
        }
    }
}

