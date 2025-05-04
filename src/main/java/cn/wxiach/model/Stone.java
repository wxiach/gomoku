package cn.wxiach.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record Stone(Point point, Color color) {
    private static final Map<String, Stone> CACHE = new ConcurrentHashMap<>();

    public static Stone of(Point point, Color color) {
        String key = point.x() + "," + point.y() + "," + color.value();
        return CACHE.computeIfAbsent(key, k -> new Stone(point, color));
    }

    public static Stone of(int x, int y, Color color) {
        return of(Point.of(x, y), color);
    }

    public int x() {
        return point.x();
    }

    public int y() {
        return point.y();
    }
}

