package cn.wxiach.core.ai.search;

import cn.wxiach.core.ai.evaluator.Evaluator;
import cn.wxiach.core.ai.evaluator.feature.FeatureBasedEvaluator;
import cn.wxiach.core.rule.BoardCheck;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.model.Point;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidatePointSearch {

    public static final int DEFAULT_SURROUNDING_RANGE = 2;

    private final char[][] board;

    private final Evaluator evaluator = new FeatureBasedEvaluator();

    public CandidatePointSearch(char[][] board) {
        this.board = board;
    }

    public List<Point> obtainCandidatePoints(Color color) {
        Map<Point, Integer> candidates = new ConcurrentHashMap<>();
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == Color.EMPTY.getValue()) continue;
                addSurroundingPoints(x, y, color, DEFAULT_SURROUNDING_RANGE, board, candidates);
            }
        }

        return candidates.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey)
                .limit(10)
                .toList();
    }

    private void addSurroundingPoints(int x, int y, Color color, int range, char[][] board, Map<Point, Integer> candidates) {
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                int nx = x + dx, ny = y + dy;
                if (BoardCheck.isEmpty(board, Point.of(nx, ny))) {
                    Piece piece = Piece.of(nx, ny, color);
                    candidates.put(piece.point(), evaluateCandidatePoint(piece));
                }
            }
        }
    }

    /**
     * Rating the board where the candidate points are located
     */
    private int evaluateCandidatePoint(Piece piece) {
        board[piece.point().x()][piece.point().y()] = piece.color().getValue();
        int score = evaluator.evaluate(board, piece.color());
        board[piece.point().x()][piece.point().y()] = Color.EMPTY.getValue();
        return score;
    }

}
