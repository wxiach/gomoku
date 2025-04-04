package cn.wxiach.core.state;

import cn.wxiach.core.rule.WinArbiter;
import cn.wxiach.core.state.support.GameStateReadable;
import cn.wxiach.model.Color;

public class GameState extends BoardState implements GameStateReadable {

    private boolean over = false;
    private Color winner = Color.EMPTY;

    public void run() {
        reset();
        this.over = false;
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
        if (!over && !boardPieces().isEmpty() && WinArbiter.checkOver(board)) {
            over = true;
            this.winner = board.lastPiece().color();
        }
        return over;
    }

    @Override
    protected void reset() {
        super.reset();
    }
}
