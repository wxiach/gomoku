package cn.wxiach.core.ai.search;

import cn.wxiach.core.ai.evaluator.Evaluator;
import cn.wxiach.core.ai.evaluator.feature.FeatureBasedEvaluator;
import cn.wxiach.core.rule.PositionCheck;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CandidatePointSearch {

    public static final int DEFAULT_SURROUNDING_RANGE = 2;

    private final int[][] board;

    public CandidatePointSearch(int[][] board) {
        this.board = board;
    }

    public List<Point> obtainCandidatePoints(Color color) {
        // Search all candidates
        Set<Candidate> candidates = ConcurrentHashMap.newKeySet();
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == 0) continue;
                addSurroundingPoints(x, y, DEFAULT_SURROUNDING_RANGE, candidates, board);
            }
        }

        // Sorted all candidates
        Evaluator evaluator = new FeatureBasedEvaluator();
        candidates.forEach(candidate -> {
            board[candidate.getPoint().x()][candidate.getPoint().y()] = color.getValue();
            int score = evaluator.evaluate(board, color);
            candidate.setScore(score);
            board[candidate.getPoint().x()][candidate.getPoint().y()] = Color.BLANK.getValue();
        });

        return candidates.stream()
                .sorted(Comparator.reverseOrder())
                .map(Candidate::getPoint)
                .limit(10)
                .toList();
    }

    private void addSurroundingPoints(int x, int y, int range, Set<Candidate> candidates, int[][] board) {
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (!PositionCheck.isOutOfBounds(Point.of(nx, ny)) && PositionCheck.isEmpty(board, Point.of(nx, ny))) {
                    candidates.add(new Candidate(Point.of(nx, ny)));
                }
            }
        }
    }

}
