package cn.wxiach.robot.search;

import cn.wxiach.core.rule.BoardCheck;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;
import cn.wxiach.robot.support.EnhancedBoard;
import cn.wxiach.robot.support.TranspositionTable;
import cn.wxiach.utils.BoardUtils;

import java.util.*;

/**
 * @author wxiach 2025/5/5
 */
public class CandidateSearch {

    // 添加offset是为了避免alpha和beta做值交换时产生数据溢出问题
    public static final int ALPHA = Integer.MIN_VALUE + 1000;
    public static final int BETA = Integer.MAX_VALUE - 1000;

    /**
     * 使用并行流执行Alpha-Beta搜索算法找出最优落子位置
     *
     * @param board 当前棋盘状态的增强表示
     * @param color 当前玩家的颜色
     * @param depth 搜索深度
     * @return 包含最佳落子位置和评估值的Result对象
     */
    public Optional<Result> execute(EnhancedBoard board, Color color, int depth) {
        List<Stone> candidateStones = obtainCandidates(board, color);

        return candidateStones.parallelStream()
                .map(move -> {
                    EnhancedBoard boardCopy = board.copy();
                    boardCopy.makeMove(move);
                    int value = -search(boardCopy, depth - 1, -BETA, -ALPHA, Color.reverse(color));
                    return new Result(move, value);
                })
                .max(Comparator.comparingInt(Result::value));
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
        if (value != null) {
            return value;
        }

        // 如果达到最大搜索深度或者当前棋局已经结束，则返回当前棋局的评估值
        // 这里检测棋局是否结束实际上是检测对手刚落的子有没有形成连五，所以checkWin这里传对手的棋子颜色
        if (depth == 0 || board.checkWin(Color.reverse(color))) {
            return board.getValue(color);
        }

        // 标记存入置换表的值类型
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

    protected List<Stone> obtainCandidates(EnhancedBoard board, Color color) {
        int boardStoneCount = BoardUtils.countStones(board);
        Set<Point> surroundPoints = searchSurroundBlankPoints(board, boardStoneCount <= 4 ? 1 : 2);

        Map<Stone, Integer> candidates = new HashMap<>();
        for (Point point : surroundPoints) {
            Stone stone = Stone.of(point, color);
            board.makeMove(stone);
            candidates.put(stone, board.getValue(color));
            board.undoMove(stone);
        }

        return candidates.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(12)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * 搜索棋盘上指定范围内的空白点
     *
     * @param board
     * @param range
     * @return
     */
    protected Set<Point> searchSurroundBlankPoints(Board board, int range) {
        Set<Point> surroundBlankPoints = new HashSet<>();
        for (int i = 0; i < board.length(); i++) {
            Stone stone = board.stone(i);
            if (stone.color() == Color.EMPTY) continue;
            surroundBlankPoints.addAll(searchSurroundBlankPoints(board, stone.point(), range));
        }
        return surroundBlankPoints;
    }

    /**
     * 搜索指定点周围的空白点
     *
     * @param board
     * @param point
     * @param range
     * @return
     */
    protected Set<Point> searchSurroundBlankPoints(Board board, Point point, int range) {
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

    public record Result(Stone stone, int value) {
    }
}
