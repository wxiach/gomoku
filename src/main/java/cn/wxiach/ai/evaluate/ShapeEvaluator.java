package cn.wxiach.ai.evaluate;

import cn.wxiach.ai.pattern.Pattern;
import cn.wxiach.model.Board;
import cn.wxiach.model.Point;

import java.util.Collection;

public interface ShapeEvaluator {

    /**
     * The chess shape evaluation is quite complicated,
     * and I can't think of a better numerical method at present,
     * so I directly return the set of chess shapes.
     *
     * @param board
     * @param point
     * @return
     */
    Collection<Pattern> evaluate(Board board, Point point);
}
