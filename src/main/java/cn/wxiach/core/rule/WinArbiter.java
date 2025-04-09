package cn.wxiach.core.rule;


import cn.wxiach.core.model.Board;
import cn.wxiach.core.utils.BoardUtils;
import cn.wxiach.robot.pattern.GomokuShapeDetector;
import cn.wxiach.robot.pattern.PatternCollection;

public class WinArbiter {
    public static boolean checkOver(Board board) {
        GomokuShapeDetector detector = GomokuShapeDetector.getInstance();
        Board opponentBoard = BoardUtils.reverseStoneColorOnBoard(board.copy());
        return detector.detect(board).contains(PatternCollection.FIVE)
                || detector.detect(opponentBoard).contains(PatternCollection.FIVE);
    }
}