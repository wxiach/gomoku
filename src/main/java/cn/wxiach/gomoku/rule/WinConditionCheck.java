package cn.wxiach.gomoku.rule;


import cn.wxiach.features.BoardFeatureDetector;
import cn.wxiach.features.pattern.Patterns;
import cn.wxiach.model.Board;
import cn.wxiach.utils.BoardUtils;

public class WinConditionCheck {
    public static boolean checkOver(Board board) {
        BoardFeatureDetector detector = BoardFeatureDetector.getInstance();
        Board opponentBoard = BoardUtils.reverseStoneColorOnBoard(board.copy());
        return detector.detect(board).contains(Patterns.A5)
                || detector.detect(opponentBoard).contains(Patterns.A5);
    }
}