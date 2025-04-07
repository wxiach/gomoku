package cn.wxiach.core.rule;


import cn.wxiach.ai.pattern.GomokuShapeDetector;
import cn.wxiach.ai.pattern.PatternCollection;
import cn.wxiach.core.utils.BoardUtils;
import cn.wxiach.model.Board;

public class WinArbiter {

    public static boolean checkOver(Board board) {
        GomokuShapeDetector detector = GomokuShapeDetector.getInstance();
        Board opponentBoard = BoardUtils.reverseStoneColorOnBoard(board.copy());
        return detector.detect(board).contains(PatternCollection.Five)
                || detector.detect(opponentBoard).contains(PatternCollection.Five);
    }
}