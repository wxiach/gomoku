package cn.wxiach.robot.search;

import cn.wxiach.gomoku.rule.BoardCheck;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wxiach 2025/5/5
 */
public class CandidateSearch {

    public Set<Point> searchSurroundBlankPoints(Board board, int range) {
        Set<Point> surroundBlankPoints = new HashSet<>();
        for (int i = 0; i < board.length(); i++) {
            Stone stone = board.stone(i);
            if (stone.color() == Color.EMPTY) continue;
            surroundBlankPoints.addAll(searchSurroundBlankPoints(board, stone.point(), range));
        }
        return surroundBlankPoints;
    }

    public Set<Point> searchSurroundBlankPoints(Board board, Point point, int range) {
        Set<Point> surroundBlankPoints = new HashSet<>();
        for (int x = Math.max(point.x() - range, 0); x <= Math.min(point.x() + range, Board.SIZE); x++) {
            for (int y = Math.max(point.y() - range, 0); y <= Math.min(point.y() + range, Board.SIZE); y++) {
                Point candidatePoint = Point.of(x, y);
                if (BoardCheck.isEmpty(board, candidatePoint)) {
                    surroundBlankPoints.add(candidatePoint);
                }
            }
        }
        return surroundBlankPoints;
    }
}
