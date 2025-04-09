package cn.wxiach.core.state;

import cn.wxiach.core.model.Color;
import cn.wxiach.core.rule.WinArbiter;

public class GameState extends BoardState implements GameStateReadable {

    private boolean over = false;
    private Color winner = Color.EMPTY;

    public void run() {
        reset();
    }

    public void end() {
        this.over = true;
    }

    @Override
    public Color winner() {
        return winner;
    }

    @Override
    public boolean isOver() {
        if (!over && WinArbiter.checkOver(board())) {
            winner = stoneSequence().getLast().color();
            over = true;
        }
        return over;
    }

    @Override
    protected void reset() {
        super.reset();
        over = false;
        winner = Color.EMPTY;
    }
}
