package cn.wxiach.ui.components;

import cn.wxiach.model.Point;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record Coordinate(int x, int y) {
    private static final Map<String, Coordinate> CACHE = new ConcurrentHashMap<>();

    public static Coordinate of(int x, int y) {
        String key = x + "," + y;
        return CACHE.computeIfAbsent(key, k -> new Coordinate(x, y));
    }

    public static Coordinate fromPoint(Point point, int unitSize, int offset) {
        int x = point.x() * unitSize + offset;
        int y = point.y() * unitSize + offset;
        return of(x, y);
    }

    public Point toPoint(int unitSize, int offset) {
        int x = Math.round((float) (x() - offset) / unitSize);
        int y = Math.round((float) (y() - offset) / unitSize);
        return Point.of(x, y);
    }

}
