package cn.wxiach;

import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.ui.GomokuWindow;

import javax.swing.*;

public class Gomoku {

    public static void main(String[] args) {
        new Gomoku().run();
    }

    public void run() {
        SwingUtilities.invokeLater(GomokuWindow::new);
        GomokuEventBus.getInstance().publish(new GameStartEvent(this));
    }
}