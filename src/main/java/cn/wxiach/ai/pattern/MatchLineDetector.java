package cn.wxiach.ai.pattern;

import cn.wxiach.model.Point;

import java.util.Collection;

public interface MatchLineDetector<T> {

    Collection<T> detect(char[][] board);

    Collection<T> detect(char[][] board, Point point);
}
