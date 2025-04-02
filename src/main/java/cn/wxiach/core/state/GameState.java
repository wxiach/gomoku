package cn.wxiach.core.state;

import cn.wxiach.core.rule.StandardWinArbiter;
import cn.wxiach.core.state.support.GameStateReadable;
import cn.wxiach.model.Color;

public class GameState extends BoardState implements GameStateReadable {

    private boolean running = false;
    private final StandardWinArbiter resultCheck = new StandardWinArbiter();

    public void run() {
        reset();
        this.running = true;
    }

    public void end() {
        this.running = false;
    }

    @Override
    public boolean isOver() {
        if (running && !boardPieces().isEmpty() && resultCheck.win(copyBoard())) {
            running = false;
        }
        return !running;
    }

    @Override
    public Color winner() {
        return resultCheck.winner();
    }

    @Override
    protected void reset() {
        super.reset();
    }
}
