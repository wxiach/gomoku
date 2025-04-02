package cn.wxiach.ai.search;

import cn.wxiach.ai.evaluate.Evaluator;
import cn.wxiach.ai.evaluate.FeatureEvaluator;
import cn.wxiach.core.rule.BoardCheck;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.model.Point;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidatePieceSearch {

    public static final int DEFAULT_SURROUNDING_RANGE = 2;

    private final Evaluator evaluator = new FeatureEvaluator();

    public CandidatePieceSearch() {
    }

    public List<Piece> obtainCandidatePoints(Board board, Color color) {
        return obtainCandidatePoints(board, color, DEFAULT_SURROUNDING_RANGE);
    }

    public List<Piece> obtainCandidatePoints(Board board, Color color, int range) {
        Map<Piece, Integer> candidates = new ConcurrentHashMap<>();
        board.pieces().forEach(piece -> {
            addSurroundingPoints(piece.point(), color, range, board, candidates);
        });

        return candidates.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey)
                .limit(10)
                .toList();
    }

    private void addSurroundingPoints(Point point, Color color, int range, Board board, Map<Piece, Integer> candidates) {
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                Piece candidate = Piece.of(point.x() + dx, point.y() + dy, color);
                if (BoardCheck.isEmpty(board.matrix(), candidate.point())) {
                    candidates.put(candidate, evaluateCandidate(candidate, board));
                }
            }
        }
    }

    /**
     * Rating the board where the candidate points are located
     */
    private int evaluateCandidate(Piece piece, Board board) {
        board.add(piece);
        int score = evaluator.evaluate(board, piece.color());
        board.remove();
        return score;
    }

}
