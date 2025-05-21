package cn.wxiach.core.store;

import cn.wxiach.core.store.state.BoardState;
import cn.wxiach.core.store.state.GameState;
import cn.wxiach.core.store.state.LevelState;
import cn.wxiach.core.store.state.TurnState;

public class GomokuStore {

    private final TurnState turnState = new TurnState();

    private final BoardState boardState = new BoardState();

    private final GameState gameState = new GameState();

    private final LevelState levelState = new LevelState();

    public TurnState getTurnState() {
        return turnState;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public LevelState getLevelState() {
        return levelState;
    }

    public void reset() {
        turnState.reset();
        boardState.reset();
        gameState.reset();
    }
}
