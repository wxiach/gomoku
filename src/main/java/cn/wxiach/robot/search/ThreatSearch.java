package cn.wxiach.robot.search;

import cn.wxiach.features.pattern.Pattern;
import cn.wxiach.features.pattern.PatternName;
import cn.wxiach.gomoku.rule.WinConditionCheck;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;
import cn.wxiach.robot.support.BoardWithZobrist;
import cn.wxiach.robot.support.TranspositionTable;
import cn.wxiach.utils.BoardUtils;

import java.util.*;

/**
 * @author wxiach 2025/5/5
 */
public class ThreatSearch extends CandidateSearch {

    public static final int THREAT_SEARCH_DEPTH = 4;

    private static final List<PatternName> attackPatterns =
            List.of(PatternName.A5, PatternName.A4, PatternName.D4, PatternName.A3);

    private static final List<PatternName> defensePatterns = List.of(PatternName.A5, PatternName.A4);

    private final TranspositionTable transpositionTable;

    public ThreatSearch(TranspositionTable transpositionTable) {
        this.transpositionTable = transpositionTable;
    }

    public int search(BoardWithZobrist board, int depth, int alpha, int beta, Color color) {

        Integer evaluation = transpositionTable.find(board.hash(), depth, alpha, beta, color);
        if (evaluation != null) {
            return evaluation;
        }

        if (depth == 0 || WinConditionCheck.checkOver(board)) {
            return evaluator.evaluate(board, color);
        }

        int evaluationType = TranspositionTable.Entry.ALPHA;

        for (Stone stone : obtainCandidates(board, color, depth % 2 == 0)) {
            board.makeMove(stone);
            int value = -search(board, depth - 1, -beta, -alpha, Color.reverse(color));
            board.undoMove(stone);

            if (value >= beta) {
                transpositionTable.store(board.hash(), beta, TranspositionTable.Entry.BETA, depth, color);
                return beta;
            }
            if (value > alpha) {
                alpha = value;
                evaluationType = TranspositionTable.Entry.EXACT;
            }
        }

        transpositionTable.store(board.hash(), alpha, evaluationType, depth, color);

        return alpha;
    }


    private List<Stone> obtainCandidates(Board board, Color color, boolean defense) {
        Map<Point, Integer> candidates = new HashMap<>();

        // 1. Add all the surrounding blank points for the default candidates
        searchSurroundBlankPoints(board, 2).forEach(point -> candidates.put(point, 0));

        // 2. Evaluate all my threat points: A5, A4, D4, A3
        scoreThreatCandidates(board.copy(), color, candidates, attackPatterns);

        // 3. If the defender, search for all opponent's threat points: A5, A4
        if (defense) scoreThreatCandidates(board.copy(), Color.reverse(color), candidates, defensePatterns);

        return candidates.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(12)
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> Stone.of(entry.getKey(), color)).toList();
    }

    private void scoreThreatCandidates(
            Board board, Color color, Map<Point, Integer> candidates, List<PatternName> patternFilter) {

        Board boardCopy = color == Color.WHITE ? BoardUtils.reverseStoneColorOnBoard(board) : board;
        candidates.forEach((point, value) -> {
            Stone stone = Stone.of(point, Color.BLACK);
            boardCopy.makeMove(stone);
            Collection<Pattern> patterns = findAllThreatPatterns(board, point, patternFilter);
            boardCopy.undoMove(stone);
            candidates.merge(point, patterns.stream().mapToInt(Pattern::value).sum(), Math::max);
        });
    }
}
