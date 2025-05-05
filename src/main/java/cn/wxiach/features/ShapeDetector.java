package cn.wxiach.features;

import cn.wxiach.features.pattern.Pattern;
import cn.wxiach.model.Board;
import cn.wxiach.model.Point;

import java.util.Collection;

public interface ShapeDetector {

    Collection<Pattern> detect(Board board);

    Collection<Pattern> detect(Board board, Point point);
}
