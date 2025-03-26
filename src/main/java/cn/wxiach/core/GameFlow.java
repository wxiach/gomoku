package cn.wxiach.core;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.*;
import cn.wxiach.model.Piece;


public class GameFlow {

    private final BoardManager boardManager = new BoardManager();
    private final TurnHandler turnHandler = new TurnHandler();
    private final RobotEngine robotEngine = new RobotEngine();

    public GameFlow() {
        GomokuEventBus.getInstance().subscribe(PieceSelectionEvent.class, event -> {
            turnHandler.setHumanPieceColor(event.getSelectedPieceColor());
            if (turnHandler.isRobotTurn()) {
                GomokuEventBus.getInstance().publish(new RobotComputeEvent(this, boardManager.getBoard()));
            }
        });

        GomokuEventBus.getInstance().subscribe(HumanClickEvent.class, event -> {

            // Block multiple quick clicks
            Piece lastPiece = boardManager.getLastPiece();
            if (lastPiece != null && lastPiece.getColor() == turnHandler.getCurrentTurn()) {
                return;
            }


            if (turnHandler.isHumanTurn()) {
                boardManager.addPiece(new Piece(event.getX(), event.getY(), turnHandler.getCurrentTurn()));
            }
        });


        GomokuEventBus.getInstance().subscribe(RobotClickEvent.class, event -> {
            if (turnHandler.isRobotTurn()) {
                boardManager.addPiece(new Piece(event.getX(), event.getY(), turnHandler.getCurrentTurn()));
            }
        });

        GomokuEventBus.getInstance().subscribe(PiecePlacedEvent.class, event -> {
            boolean isGameOver = GameRule.checkGameOver(boardManager.getBoard());
            if (isGameOver) {
                Piece.Color winner = boardManager.getLastPiece().getColor();
                GomokuEventBus.getInstance().publish(new GameOverEvent(this, winner));
            } else {
                turnHandler.switchTurn();
                if (turnHandler.isRobotTurn()) {
                    GomokuEventBus.getInstance().publish(new RobotComputeEvent(this, boardManager.getBoard()));
                }
            }
        });
    }

    public void startGame() {
        GomokuEventBus.getInstance().publish(new GameStartEvent(this));
    }


}
