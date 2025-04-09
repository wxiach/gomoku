package cn.wxiach.robot.pattern;

import cn.wxiach.core.model.Board;
import cn.wxiach.core.model.Point;

import java.util.Collection;

public interface ShapeDetector {

    Collection<Pattern> detect(Board board);

    Collection<Pattern> detect(Board board, Point point);
}
