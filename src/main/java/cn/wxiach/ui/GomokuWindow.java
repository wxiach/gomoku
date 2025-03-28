package cn.wxiach.ui;


import cn.wxiach.core.GameFlow;
import cn.wxiach.ui.components.GameMessageBox;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.*;
import cn.wxiach.model.Color;
import cn.wxiach.ui.components.GomokuPanel;

import javax.swing.*;

public class GomokuWindow extends JFrame {

    private final GameFlow gameFlow = new GameFlow();
    private final GomokuPanel gomokuPanel = new GomokuPanel();

    public GomokuWindow() {
        setTitle("Gomoku - Battle with AI");
        add(gomokuPanel);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GomokuEventBus.getInstance().subscribe(GameStartEvent.class, event -> {
            this.setVisible(true);
            Color selectedPieceColor = GameMessageBox.showPieceSelectionDialog(this);
            GomokuEventBus.getInstance().publish(new PieceSelectionEvent(this, selectedPieceColor));
        });

        GomokuEventBus.getInstance().subscribe(GameOverEvent.class, event -> {
            boolean isRestart = GameMessageBox.showGameOverOptionDialog(this, event.getWinner());
            if (isRestart) {
                GomokuEventBus.getInstance().publish(new GameRestartEvent(this));
            }
        });

        GomokuEventBus.getInstance().subscribe(GameExitEvent.class, event -> {
            this.dispose();
        });
    }

    public GomokuPanel getGomokuPanel() {
        return gomokuPanel;
    }

    public GameFlow getGameFlow() {
        return gameFlow;
    }
}
