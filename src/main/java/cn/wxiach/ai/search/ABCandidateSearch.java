package cn.wxiach.ai.search;

import cn.wxiach.ai.evaluate.FeatureEvaluator;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.model.Point;

import java.util.*;

public class ABCandidateSearch implements AroundCandidateSearch {

    private final FeatureEvaluator evaluator;

    public ABCandidateSearch(FeatureEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public List<Piece> obtainCandidates(Board board, Color color) {
        Set<Point> surroundBlankPoint = new HashSet<>();
        board.pieces().forEach(piece -> {
            surroundBlankPoint.addAll(searchSurroundBlankPoint(piece.point(), board));
        });

        Map<Piece, Integer> candidateScoreMap = new HashMap<>();
        surroundBlankPoint.forEach(point -> {
            Piece piece = Piece.of(point, color);
            board.addPiece(piece);
            candidateScoreMap.put(piece, evaluator.evaluate(board, color));
            board.removeLastPiece();
        });

        Comparator<Map.Entry<Piece, Integer>> comparator = Comparator
                .comparing((Map.Entry<Piece, Integer> entry) -> -entry.getValue());

        return candidateScoreMap.entrySet().stream().sorted(comparator).map(Map.Entry::getKey).toList();
    }

}
