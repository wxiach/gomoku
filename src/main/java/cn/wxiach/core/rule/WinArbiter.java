package cn.wxiach.core.rule;


import cn.wxiach.ai.evaluate.GomokuEvaluator;
import cn.wxiach.ai.evaluate.ShapeEvaluator;
import cn.wxiach.ai.pattern.Pattern;
import cn.wxiach.ai.pattern.PatternCollection;
import cn.wxiach.model.Board;

import java.util.Collection;

public class WinArbiter {

    private final static ShapeEvaluator evaluator = new GomokuEvaluator();

    public static boolean checkWin(Board board) {
        Collection<Pattern> patterns = evaluator.evaluate(board, board.lastPiece().point());
        long a5 = patterns.stream().filter(pattern -> PatternCollection.A5.equals(pattern.name())).count();
        long a4 = patterns.stream().filter(pattern -> PatternCollection.A4.equals(pattern.name())).count();
        long d4 = patterns.stream().filter(pattern -> PatternCollection.D4.equals(pattern.name())).count();
        long a3 = patterns.stream().filter(pattern -> PatternCollection.A3.equals(pattern.name())).count();
        return (a5 > 0L || a4 > 0L || (d4 > 1L) || (d4 > 0L && a3 > 0L) || (a3 > 1L));
    }

    public static boolean checkOver(Board board) {
        Collection<Pattern> patterns = evaluator.evaluate(board, board.lastPiece().point());
        return patterns.contains(PatternCollection.Five);
    }
}
