package cn.wxiach.gomoku.rule;


import cn.wxiach.model.Board;
import cn.wxiach.robot.features.GomokuShapeDetector;
import cn.wxiach.robot.features.PatternCollection;
import cn.wxiach.utils.BoardUtils;

public class WinArbiter {
    public static boolean checkOver(Board board) {
        GomokuShapeDetector detector = GomokuShapeDetector.getInstance();
        Board opponentBoard = BoardUtils.reverseStoneColorOnBoard(board.copy());
        return detector.detect(board).contains(PatternCollection.FIVE)
                || detector.detect(opponentBoard).contains(PatternCollection.FIVE);
    }
}