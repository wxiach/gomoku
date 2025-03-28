package cn.wxiach.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record Point(int x, int y) {
    private static final Map<String, Point> CACHE = new ConcurrentHashMap<>();

    public static Point of(int x, int y) {
        String key = x + "," + y;
        return CACHE.computeIfAbsent(key, k -> new Point(x, y));
    }
}

