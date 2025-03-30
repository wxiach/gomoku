package cn.wxiach.core.state;

import cn.wxiach.core.ai.pattern.Pattern;
import cn.wxiach.core.state.rule.GameStateCheck;
import cn.wxiach.core.state.support.GameStateReadable;
import cn.wxiach.model.Color;

import java.util.List;

public class GameState extends BoardState implements GameStateCheck, GameStateReadable {

    private Color winner = Color.EMPTY;
    private boolean running = false;

    public void run() {
        reset();
        this.running= true;
    }

    public void end() {
        this.running= false;
    }

    @Override
    public boolean isOver() {
        if (!running) return true;
        if (pieces().isEmpty()) return false;
        Color color = pieces().getLast().color();
        List<Pattern<String>> patterns = featurePatternDetector.detect(board(), color);
        if (patterns.contains(color == Color.WHITE ? WHITE_WIN_CONDITION : BLACK_WIN_CONDITION)) {
            winner = color;
            end();
            return true;
        }
        return false;
    }

    @Override
    public Color winner() {
        return winner;
    }

    @Override
    protected void reset() {
        super.reset();
    }
}
