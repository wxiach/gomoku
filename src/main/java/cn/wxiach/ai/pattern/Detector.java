package cn.wxiach.ai.pattern;

import cn.wxiach.model.Point;

import java.util.Collection;

public interface Detector<T> {

    Collection<Pattern<T>> detect(char[][] board);

    Collection<Pattern<T>> detect(char[][] board, Point point);
}
