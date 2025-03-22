package cn.wxiach.ui;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.GameStartEvent;

import javax.swing.*;

public class GomokuWindow extends JFrame {

    private final BoardPanel boardPanel = new BoardPanel();

    public GomokuWindow() {
        setTitle("Gomoku - Battle with AI");
        add(boardPanel);

        GomokuEventBus.getInstance().subscribe(GameStartEvent.class, event -> {
            this.setVisible(true);
        });
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}
