package cn.wxiach.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record Stone(Point point, Color color) {
    private static final Map<String, Stone> CACHE = new ConcurrentHashMap<>();

    public static Stone of(Point point, Color color) {
        String key = point.x() + "," + point.y() + "," + color.value();
        return CACHE.computeIfAbsent(key, k -> new Stone(point, color));
    }
}

