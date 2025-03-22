package cn.wxiach.ui;

import cn.wxiach.domain.Board;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.GameStartEvent;

import javax.swing.*;

public class GomokuWindow extends JFrame {

    private final BoardPanel boardPanel = new BoardPanel();
    private final Board board = new Board();

    public GomokuWindow() {
        setTitle("Gomoku - Battle with AI");
        add(boardPanel);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GomokuEventBus.getInstance().subscribe(GameStartEvent.class, event -> {
            this.setVisible(true);
        });
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}
