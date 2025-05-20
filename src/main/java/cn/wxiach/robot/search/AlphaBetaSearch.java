package cn.wxiach.robot.search;

import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;
import cn.wxiach.pattern.Patterns;
import cn.wxiach.robot.support.EnhancedBoard;
import cn.wxiach.robot.support.TranspositionTable;
import cn.wxiach.utils.BoardUtils;
import cn.wxiach.utils.MathUtils;

import java.util.*;

/**
 * Parallel Alpha-Beta search for Gomoku.
 */
public class AlphaBetaSearch extends CandidateSearch {
    private final ThreatSearch threatSearch = new ThreatSearch();

    /**
     * 使用并行流执行Alpha-Beta搜索算法找出最优落子位置
     *
     * @param board 当前棋盘状态的增强表示
     * @param color 当前玩家的颜色
     * @param depth 搜索深度
     * @return 包含最佳落子位置和评估值的Result对象
     */
    public Result execute(EnhancedBoard board, Color color, int depth) {

        // 搜索深度添加算杀深度
        int newDepth = depth + ThreatSearch.THREAT_SEARCH_DEPTH;

        // 添加offset是为了避免alpha和beta做值交换时产生数据溢出问题
        int alpha = Integer.MIN_VALUE + 1000;
        int beta = Integer.MAX_VALUE - 1000;

        List<Stone> candidateStones = obtainCandidates(board, color);

        return candidateStones.parallelStream()
                .map(move -> {
                    EnhancedBoard boardCopy = board.copy();
                    boardCopy.makeMove(move);
                    int value = -search(boardCopy, newDepth - 1, -beta, -alpha, Color.reverse(color));
                    return new Result(move, value);
                })
                .max(Comparator.comparingInt(Result::value))
                .orElseGet(() -> new Result(candidateStones.getFirst(), Integer.MIN_VALUE));
    }

    /**
     * Alpha-Beta搜索算法的核心实现方法
     * <p>
     * 该方法使用Alpha-Beta剪枝技术执行极小化极大搜索，通过递归探索博弈树以确定最佳走法。
     * 算法使用置换表优化查询性能，并在特定深度使用威胁搜索进行局部评估。
     *
     * @param board 当前棋盘状态的增强表示
     * @param depth 当前搜索的剩余深度
     * @param alpha Alpha值，表示当前玩家已知的最小得分下限
     * @param beta  Beta值，表示对手已知的最大得分上限
     * @param color 当前玩家的颜色
     * @return 当前局面对于当前玩家的最佳评分
     */
    private int search(EnhancedBoard board, int depth, int alpha, int beta, Color color) {
        // 检查置换表中是否存在当前棋局的评估值，如果存在则直接返回
        Integer value = TranspositionTable.find(board.getHash(), depth, alpha, beta, color);
        if (value != null) return value;

        // 这里实际上是检测对手刚落的子有没有形成连五，所以这里传对手的棋子颜色
        if (board.checkWin(Color.reverse(color))) return board.getValue(color);

        // 达到指定搜索深度后，开启算杀
        if (depth == ThreatSearch.THREAT_SEARCH_DEPTH) {
            value = threatSearch.search(board, depth, alpha, beta, color);

            // 如果算杀成功，则直接返回算杀函数的评估值
            if (MathUtils.approximateEqual(value, Patterns.A5.value(), 1.2)) {
                return value;
            }

            // 否则，返回算杀前的棋局的评估值
            return board.getValue(color);
        }

        int valueType = TranspositionTable.Entry.ALPHA;

        for (Stone stone : obtainCandidates(board, color)) {
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

    private List<Stone> obtainCandidates(EnhancedBoard board, Color color) {
        int boardStoneCount = BoardUtils.countStones(board);
        Set<Point> surroundPoints = searchSurroundBlankPoints(board, boardStoneCount <= 4 ? 1 : 2);

        Map<Stone, Integer> candidateScoreMap = new HashMap<>();
        for (Point point : surroundPoints) {
            Stone stone = Stone.of(point, color);
            board.makeMove(stone);
            candidateScoreMap.put(stone, board.getValue(color));
            board.undoMove(stone);
        }

        Comparator<Map.Entry<Stone, Integer>> comparator = Comparator
                .comparing((Map.Entry<Stone, Integer> entry) -> -entry.getValue());

        return candidateScoreMap.entrySet().stream()
                .sorted(comparator).limit(12).map(Map.Entry::getKey).toList();
    }

    public record Result(Stone stone, int value) {
    }

}
