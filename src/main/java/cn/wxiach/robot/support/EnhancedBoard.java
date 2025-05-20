package cn.wxiach.robot.support;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;
import cn.wxiach.pattern.Pattern;
import cn.wxiach.pattern.PatternMatcherUtils;
import cn.wxiach.pattern.Patterns;
import cn.wxiach.robot.evaluation.BoardEvaluator;
import cn.wxiach.robot.evaluation.BoardLineTable;

import java.util.*;

/**
 * @author wxiach 2025/5/9
 */
public class EnhancedBoard extends Board implements BoardEvaluator {

    private final Map<Integer, Pattern> blackPatternMap = new HashMap<>();
    private final Map<Integer, Pattern> whitePatternMap = new HashMap<>();

    // 棋盘Zobrist哈希值
    private long hash;

    // 存储每一条棋盘线的分值，这样便于增量式计算棋盘的评分
    // 数组中每一项的分值是黑棋的棋形得分减去白棋的棋形得分
    private int[] values = new int[BoardLineTable.BOARD_LINES.size()];

    public EnhancedBoard(Board board) {
        super(board);
        this.hash = ZobristHash.compute(board);
        this.initBoardEvaluation();
    }

    public EnhancedBoard(EnhancedBoard other) {
        super(other);
        this.hash = other.hash;
        this.values = Arrays.copyOf(other.values, other.values.length);
        this.blackPatternMap.putAll(other.blackPatternMap);
        this.whitePatternMap.putAll(other.whitePatternMap);
    }


    @Override
    public void makeMove(Stone stone) {
        super.makeMove(stone);
        hash = ZobristHash.update(hash, stone);
        evaluate(stone);
    }

    @Override
    public void undoMove(Stone stone) {
        super.undoMove(stone);
        hash = ZobristHash.update(hash, stone);
        evaluate(stone);
    }


    @Override
    public void evaluate(Stone stone) {
        // 根据棋盘坐标查询棋盘线索引
        int stoneIndex = Board.index(stone.point());
        BoardLineTable.LineIndices lineIndices = BoardLineTable.POINT_LINE_MAPPING[stoneIndex];

        // 评估查询到的每一条棋盘线
        for (int index : lineIndices.indices()) {
            evaluateBoardLine(index);
        }
    }

    /**
     * 遍历所有棋盘线路并进行评估，设置棋盘的初始评分
     */
    private void initBoardEvaluation() {
        for (int i = 0; i < BoardLineTable.BOARD_LINES.size(); i++) {
            evaluateBoardLine(i);
        }
    }

    /**
     * 评估指定棋盘线的局势
     * 该方法会获取棋盘线上的所有棋子，并进行模式匹配，计算黑白双方在该线上的价值
     *
     * @param index 棋盘线索引
     */
    private void evaluateBoardLine(int index) {
        // 根据棋盘线索引从BoardLineTable获取对应的具体棋盘线
        int[] boardLine = BoardLineTable.BOARD_LINES.get(index);

        // 如果棋盘线长度小于5，则跳过，因为没必要评估
        if (boardLine.length < 5) {
            return;
        }

        // 将棋盘线转换为字符串方便进行模式匹配
        StringBuilder stoneStrBuilder = new StringBuilder(boardLine.length);
        for (int i : boardLine) {
            stoneStrBuilder.append(get(i));
        }
        String stoneStr = stoneStrBuilder.toString();

        // 匹配黑棋的棋形
        Optional<Pattern> matchBlackPattern = PatternMatcherUtils.matchBlackPattern(stoneStr);
        blackPatternMap.put(index, matchBlackPattern.orElse(null));

        // 匹配白棋的棋形
        Optional<Pattern> matchWhitePattern = PatternMatcherUtils.matchWhitePattern(stoneStr);
        whitePatternMap.put(index, matchWhitePattern.orElse(null));

        // 计算评估值
        int blackValue = matchBlackPattern.map(Pattern::value).orElse(0);
        int whiteValue = matchWhitePattern.map(Pattern::value).orElse(0);
        values[index] = blackValue - whiteValue;
    }


    /**
     * 获取特定颜色方的棋盘评估分数。
     *
     * @param color 要评估的棋子颜色（黑方或白方）
     * @return 正值表示对指定颜色方有利，负值表示不利，零表示均势
     */
    public int getValue(Color color) {
        int totalValue = Arrays.stream(values).sum();
        return color == Color.BLACK ? totalValue : -totalValue;
    }

    public boolean checkWin(Color color) {
        Collection<Pattern> patterns = color == Color.BLACK ? blackPatternMap.values() : whitePatternMap.values();
        return patterns.stream().anyMatch(pattern -> pattern == Patterns.A5);
    }

    public Collection<Pattern> getPatterns(Stone stone) {
        Point stonePoint = stone.point();

        // 拿到棋盘线的索引
        int stoneIndex = Board.index(stonePoint);
        BoardLineTable.LineIndices boardLineIndices = BoardLineTable.POINT_LINE_MAPPING[stoneIndex];

        // 拿到棋子颜色，确定棋形集合
        Color stoneColor = stone(stoneIndex).color();
        Map<Integer, Pattern> patterns = stoneColor == Color.BLACK ? blackPatternMap : whitePatternMap;

        // 根据棋盘线索引对棋形集合进行过滤，只保留在该棋盘线索引上的棋形
        return boardLineIndices.indices().stream().map(patterns::get).filter(Objects::nonNull).toList();
    }

    public long getHash() {
        return hash;
    }

    public EnhancedBoard copy() {
        return new EnhancedBoard(this);
    }
}
