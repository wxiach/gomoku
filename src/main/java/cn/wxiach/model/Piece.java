package cn.wxiach.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record Piece(Point point, Color color) {
    private static final Map<String, Piece> CACHE = new ConcurrentHashMap<>();

    public static Piece of(Point point, Color color) {
        String key = point.x() + "," + point.y() + "," + color.getValue();
        return CACHE.computeIfAbsent(key, k -> new Piece(point, color));
    }

    public static Piece of(int x, int y, Color color) {
        return of(Point.of(x, y), color);
    }
}

