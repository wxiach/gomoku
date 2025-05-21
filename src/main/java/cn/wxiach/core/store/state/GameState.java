package cn.wxiach.core.store.state;

import cn.wxiach.model.Color;

/**
 * @author wxiach 2025/5/5
 */
public class GameState {

    private boolean over = false;

    private Color winner = Color.EMPTY;

    public boolean isOver() {
        return over;
    }

    public void setOver() {
        this.over = true;
    }

    public Color getWinner() {
        return winner;
    }

    public void setWinner(Color winner) {
        this.winner = winner;
    }

    public void reset() {
        this.over = false;
        this.winner = Color.EMPTY;
    }
}
