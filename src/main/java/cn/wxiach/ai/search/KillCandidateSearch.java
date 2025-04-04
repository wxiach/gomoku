package cn.wxiach.ai.search;

import cn.wxiach.ai.evaluate.ShapeEvaluator;
import cn.wxiach.ai.pattern.Pattern;
import cn.wxiach.ai.pattern.PatternCollection;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.model.Point;

import java.util.*;
import java.util.stream.Collectors;

public class KillCandidateSearch implements AroundCandidateSearch {

    private final ShapeEvaluator evaluator;

    public KillCandidateSearch(ShapeEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public List<Piece> obtainCandidates(Board board, Color color) {
        Set<Point> surroundBlankPoint = new HashSet<>();
        board.pieces().forEach(piece -> {
            surroundBlankPoint.addAll(searchSurroundBlankPoint(piece.point(), board));
        });

        List<String> defenseCandidateTypes = List.of(PatternCollection.A5, PatternCollection.A4);
        List<String> attackCandidateTypes = List.of(PatternCollection.A5, PatternCollection.A4,
                PatternCollection.D4, PatternCollection.A3);
        Map<String, List<Piece>> defenseCandidatesMap = new HashMap<>();
        Map<String, List<Piece>> attackCandidatesMap = new HashMap<>();
        defenseCandidateTypes.forEach(type -> defenseCandidatesMap.put(type, new ArrayList<>()));
        attackCandidateTypes.forEach(type -> attackCandidatesMap.put(type, new ArrayList<>()));

        for (Point point : surroundBlankPoint) {

            // First, stand in the opponent's position to identify defensive point.
            board.addPiece(Piece.of(point, Color.reverse(color)));
            Collection<Pattern> patternsForDefense = evaluator.evaluate(board, point).stream()
                    .filter(pattern -> PatternCollection.A5.equals(pattern.name())
                            || PatternCollection.A4.equals(pattern.name()))
                    .toList();
            board.removeLastPiece();

            // Then stand in your own position to identify attack points.
            board.addPiece(Piece.of(point, color));
            Collection<Pattern> patternsForAttack = evaluator.evaluate(board, point);
            board.removeLastPiece();

            if (patternsForDefense.isEmpty() && patternsForAttack.isEmpty()) continue;

            // If patternsForAttack contains A5, it indicates a winning move, so return immediately.
            if (patternsForAttack.contains(PatternCollection.Five)) {
                return List.of(Piece.of(point, color));
            }

            // If a point serves as both a defensive and an attack point, return immediately as it is an optimal choice.
            if (!patternsForDefense.isEmpty() && !patternsForAttack.isEmpty()) {
                return List.of(Piece.of(point, color));
            }

            // If patternsForDefense is not empty, prioritize defense as it is crucial.
            if (!patternsForDefense.isEmpty()) {
                Map<String, Long> patternCounts = patternsForDefense.stream()
                        .collect(Collectors.groupingBy(Pattern::name, Collectors.counting()));

                for (String type : defenseCandidateTypes) {
                    if (patternCounts.getOrDefault(type, 0L) > 0) {
                        defenseCandidatesMap.get(type).add(Piece.of(point, color));
                        break;
                    }
                }
            }

            // If the code reaches this point, it indicates that an attack is possible.
            if (!patternsForAttack.isEmpty()) {

                // Add the point to the A4 list if it has two or more threat types.
                if (patternsForAttack.size() >= 2) {
                    attackCandidatesMap.get(PatternCollection.A4).addFirst(Piece.of(point, color));
                    continue;
                }

                Map<String, Long> patternCounts = patternsForAttack.stream()
                        .collect(Collectors.groupingBy(Pattern::name, Collectors.counting()));

                for (String type : attackCandidateTypes) {
                    if (patternCounts.getOrDefault(type, 0L) > 0) {
                        attackCandidatesMap.get(type).addLast(Piece.of(point, color));
                        break;
                    }
                }
            }
        }

        List<Piece> defenseCandidates = defenseCandidateTypes.stream().map(defenseCandidatesMap::get).flatMap(List::stream).toList();
        if (!defenseCandidates.isEmpty()) return defenseCandidates;

        return attackCandidateTypes.stream().map(attackCandidatesMap::get).flatMap(List::stream).toList();
    }
}
