package cn.wxiach.core;

import cn.wxiach.core.ai.RobotEngine;
import cn.wxiach.core.rule.GameStateCheck;
import cn.wxiach.core.rule.RuleEngine;
import cn.wxiach.core.state.BoardState;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.*;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;


public class GameFlow {

    private final RuleEngine ruleEngine;
    private final BoardState boardState;

    public GameFlow() {

        this.ruleEngine = new RuleEngine();
        this.boardState = new BoardState();
        RobotEngine robotEngine = new RobotEngine(boardState);

        GomokuEventBus.getInstance().subscribe(PieceSelectionEvent.class, event -> {
            boardState.setHumanColor(event.getSelectedPieceColor());

            // If human choose white piece, robot move first.
            if (ruleEngine.isRobotTurn(boardState.getRobotColor())) {
                GomokuEventBus.getInstance().publish(new RobotComputeEvent(this, boardState.getBoard()));
            }
        });

        GomokuEventBus.getInstance().subscribe(HumanClickEvent.class, event -> {
            // Block multiple quick clicks
            Piece lastPiece = boardState.getLastPiece();
            if (lastPiece != null && lastPiece.color() == ruleEngine.getCurrentTurn()) {
                return;
            }

            if (ruleEngine.isHumanTurn(boardState.getHumanColor())){
                boardState.addPiece(Piece.of(event.getPoint(), ruleEngine.getCurrentTurn()));
            }
        });


        GomokuEventBus.getInstance().subscribe(RobotClickEvent.class, event -> {
            if (ruleEngine.isRobotTurn(boardState.getRobotColor())) {
                boardState.addPiece(Piece.of(event.getPoint(), ruleEngine.getCurrentTurn()));
            }
        });

        GomokuEventBus.getInstance().subscribe(PiecePlacedEvent.class, event -> {
            boolean isGameOver = GameStateCheck.isGameOver(boardState.getBoard());
            if (isGameOver) {
                Color winner = boardState.getLastPiece().color();
                GomokuEventBus.getInstance().publish(new GameOverEvent(this, winner));
            } else {
                ruleEngine.switchTurn();
                if (ruleEngine.isRobotTurn(boardState.getRobotColor())) {
                    GomokuEventBus.getInstance().publish(new RobotComputeEvent(this, boardState.getBoard()));
                }
            }
        });
    }

    public void startGame() {
        GomokuEventBus.getInstance().publish(new GameStartEvent(this));
    }

}
