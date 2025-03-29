package cn.wxiach.core;

import cn.wxiach.core.ai.RobotEngine;
import cn.wxiach.core.rule.GameStateCheck;
import cn.wxiach.core.rule.RuleEngine;
import cn.wxiach.core.state.BoardState;
import cn.wxiach.core.state.PieceColorState;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.*;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.model.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameFlow {

    private static final Logger logger = LoggerFactory.getLogger(GameFlow.class);

    private final RuleEngine ruleEngine;
    private final BoardState boardState;
    private final RobotEngine robotEngine;

    public GameFlow() {

        this.ruleEngine = new RuleEngine();
        this.boardState = new BoardState();
        this.robotEngine = new RobotEngine();

        GomokuEventBus.getInstance().subscribe(PieceColorSelectEvent.class, event -> {
            logger.info("Game start.");
            // Set the color of the chess pieces for the human and the robot respectively
            Color robotColor = PieceColorState.reverseColor(event.getHumanColor());
            boardState.setColor(event.getHumanColor(), robotColor);
            robotEngine.setColor(robotColor);

            // If human choose white piece, robot move first.
            if (ruleEngine.isRobotTurn(boardState.getRobotColor())) {
                GomokuEventBus.getInstance().publish(new RobotMoveEvent(this, Point.of(7, 7)));
            }
        });

        GomokuEventBus.getInstance().subscribe(HumanClickEvent.class, event -> {
            logger.debug("Human clicked at ({}, {}).", event.getPoint().x(), event.getPoint().y());

            // Prevent multiple quick clicks
            if (!boardState.pieces().isEmpty() && boardState.pieces().getLast().color() == ruleEngine.getCurrentTurn()) {
                return;
            }

            if (ruleEngine.isHumanTurn(boardState.getHumanColor())) {
                boardState.addPiece(Piece.of(event.getPoint(), ruleEngine.getCurrentTurn()));
            }
        });


        GomokuEventBus.getInstance().subscribe(RobotMoveEvent.class, event -> {
            if (ruleEngine.isRobotTurn(boardState.getRobotColor())) {
                boardState.addPiece(Piece.of(event.getPoint(), ruleEngine.getCurrentTurn()));
            }
        });

        GomokuEventBus.getInstance().subscribe(PiecePlacedEvent.class, event -> {
            boolean isGameOver = GameStateCheck.isGameOver(boardState.board());
            if (isGameOver) {
                Color winner = boardState.pieces().getLast().color();
                GomokuEventBus.getInstance().publish(new GameOverEvent(this, winner));
            } else {
                ruleEngine.switchTurn();
                if (ruleEngine.isRobotTurn(boardState.getRobotColor())) {
                    logger.info("Robot start thinking.");
                    robotEngine.startCompute(boardState.board());
                }
            }
        });
    }

    public void startGame() {
        GomokuEventBus.getInstance().publish(new GameStartEvent(this));
    }

}
