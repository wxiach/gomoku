package cn.wxiach.robot.search;

import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;
import cn.wxiach.pattern.Pattern;
import cn.wxiach.pattern.PatternName;
import cn.wxiach.robot.support.EnhancedBoard;
import cn.wxiach.robot.support.TranspositionTable;

import java.util.*;

/**
 * @author wxiach 2025/5/5
 */
public class ThreatSearch extends CandidateSearch {

    public static final int THREAT_SEARCH_DEPTH = 8;

    private final ThreatPatternFilter threatPatternFilter = new ThreatPatternFilter();

    public int search(EnhancedBoard board, int depth, int alpha, int beta, Color color) {
        Integer value = TranspositionTable.find(board.getHash(), depth, alpha, beta, color);
        if (value != null) return value;

        if (depth == 0 || board.checkWin(Color.reverse(color))) {
            return board.getValue(color);
        }

        int valueType = TranspositionTable.Entry.ALPHA;

        for (Stone stone : obtainCandidates(board, color, depth % 2 == 0)) {
            board.makeMove(stone);
            value = -search(board, depth - 1, -beta, -alpha, Color.reverse(color));
            board.undoMove(stone);

            if (value >= beta) {
                TranspositionTable.store(board.getHash(), beta, TranspositionTable.Entry.BETA, depth, color);
                return beta;
            }
            if (value > alpha) {
                alpha = value;
                valueType = TranspositionTable.Entry.EXACT;
            }
        }

        TranspositionTable.store(board.getHash(), alpha, valueType, depth, color);

        return alpha;
    }


    /**
     * 获取当前玩家可落子的候选点列表。
     * 该方法根据不同点的威胁值评估棋盘上的位置：
     * 1. 首先添加所有周围的空白点作为默认候选点
     * 2. 然后评估当前玩家的进攻威胁
     * 3. 如果处于防守模式，还会评估对手的威胁点以进行阻挡
     *
     * @param board   当前棋盘状态
     * @param color   当前玩家的棋子颜色
     * @param defense 是否为防守模式（评估对手威胁）
     * @return 经过排序和过滤的候选落子点列表
     */
    private List<Stone> obtainCandidates(EnhancedBoard board, Color color, boolean defense) {
        Map<Point, Integer> candidates = new HashMap<>();

        // 1. Add all the surrounding blank points for the default candidates
        searchSurroundBlankPoints(board, 2).forEach(point -> candidates.put(point, 0));

        // 2. Evaluate all my threat points: A5, A4, D4, A3
        scoreThreatCandidates(board.copy(), color, candidates, false);

        // 3. If the defender, search for all opponent's threat points: A5, A4
        if (defense) scoreThreatCandidates(board.copy(), Color.reverse(color), candidates, true);

        return candidates.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(12)
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> Stone.of(entry.getKey(), color)).toList();
    }

    private void scoreThreatCandidates(EnhancedBoard board, Color color, Map<Point, Integer> candidates, boolean defense) {

        candidates.forEach((point, value) -> {
            Stone stone = Stone.of(point, color);
            board.makeMove(stone);
            Collection<Pattern> patterns = board.getPatterns(stone);
            board.undoMove(stone);

            patterns = threatPatternFilter.filter(patterns, defense);
            candidates.merge(point, patterns.stream().mapToInt(Pattern::value).sum(), Math::max);
        });
    }

    public static class ThreatPatternFilter {

        public static final List<PatternName> ATTACK_PATTERNS = List.of(
                PatternName.A5,
                PatternName.A4,
                PatternName.D4,
                PatternName.A3
        );

        private static final List<PatternName> DEFENSE_PATTERNS = List.of(
                PatternName.A5,
                PatternName.A4
        );

        public Collection<Pattern> filter(Collection<Pattern> patterns, boolean defense) {
            if (defense) {
                return filter(patterns, DEFENSE_PATTERNS);
            }
            return filter(patterns, ATTACK_PATTERNS);
        }

        private Collection<Pattern> filter(Collection<Pattern> patterns, Collection<PatternName> filter) {
            if (patterns == null || patterns.isEmpty()) {
                return Collections.emptyList();
            }

            return patterns.stream().filter(pattern -> filter.contains(pattern.name())).toList();
        }
    }
}
