package cn.wxiach.core.rule;

import cn.wxiach.ai.pattern.Pattern;
import cn.wxiach.ai.pattern.PatternCollection;
import cn.wxiach.ai.pattern.feature.FeatureDetector;
import cn.wxiach.ai.pattern.feature.FeaturePatternCollection;
import cn.wxiach.core.utils.BoardUtils;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;

public class StandardWinArbiter implements WinArbiter {

    private final FeatureDetector featureDetector = new FeatureDetector();
    private final PatternCollection<String> patternCollection = new FeaturePatternCollection();

    private Color winner = Color.EMPTY;

    @Override
    public boolean win(Board board) {
        char[][] boardMatrix = board.matrix();

        if ( board.last().color() == Color.WHITE) {
            boardMatrix = BoardUtils.revertPieceColorOfBoardMatrix(boardMatrix);
        }
        if (featureDetector.detect(boardMatrix).contains(patternCollection.win())) {
            winner = board.last().color();
            return true;
        }
        return false;
    }

    public Color winner() {
        return winner;
    }

    public Pattern<String> winCondition() {
        return patternCollection.win();
    }
}
