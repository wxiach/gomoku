package cn.wxiach.ui;

import javax.swing.*;

public class GomokuWindow {

    public GomokuWindow() {
        JFrame frame = new JFrame("Gomoku - Battle with AI");
        frame.add(new BoardPanel());
        frame.setVisible(true);
    }

    public static void run() {
        new GomokuWindow();
    }
}
