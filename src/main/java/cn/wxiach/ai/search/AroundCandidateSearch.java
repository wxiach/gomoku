package cn.wxiach.ai.search;

import cn.wxiach.core.rule.BoardCheck;
import cn.wxiach.model.Board;
import cn.wxiach.model.Point;

import java.util.ArrayList;
import java.util.Collection;

interface AroundCandidateSearch {
    int range = 2;

    default Collection<Point> searchSurroundBlankPoint(Point point, Board board) {
        Collection<Point> blankPoints = new ArrayList<>();
        for (int x = Math.max(point.x() - range, 0); x < Math.min(point.x() + range, Board.SIZE); x++) {
            for (int y = Math.max(point.y() - range, 0); y < Math.min(point.y() + range, Board.SIZE); y++) {
                Point candidatePoint = Point.of(x, y);
                if (BoardCheck.isEmpty(board, candidatePoint)) blankPoints.add(candidatePoint);
            }
        }
        return blankPoints;
    }
}
