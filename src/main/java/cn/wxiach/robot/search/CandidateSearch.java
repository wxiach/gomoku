package cn.wxiach.robot.search;

import cn.wxiach.features.BoardFeatureDetector;
import cn.wxiach.features.pattern.Pattern;
import cn.wxiach.features.pattern.PatternName;
import cn.wxiach.gomoku.rule.BoardCheck;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.robot.evaluation.GomokuEvaluator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wxiach 2025/5/5
 */
public class CandidateSearch {

    protected final GomokuEvaluator evaluator = new GomokuEvaluator();

    public Set<Point> searchSurroundBlankPoints(Board board, int range) {
        Set<Point> surroundBlankPoints = new HashSet<>();
        for (int i = 0; i < board.length(); i++) {
            if (board.color(i) == Color.EMPTY) continue;
            surroundBlankPoints.addAll(searchSurroundBlankPoints(board, (board.point(i)), range));
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

    public static Collection<Pattern> findAllThreatPatterns(Board board, Point point, List<PatternName> patternFilter) {
        Collection<Pattern> patterns = BoardFeatureDetector.getInstance().detect(board, point);
        return patterns.stream().filter(pattern -> patternFilter.contains(pattern.name())).toList();
    }
}
