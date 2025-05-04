package cn.wxiach.ui.window;


import cn.wxiach.core.GomokuFlow;
import cn.wxiach.ui.action.RevertButton;
import cn.wxiach.ui.action.StartGameButton;
import cn.wxiach.ui.action.SurrenderButton;
import cn.wxiach.ui.board.GomokuBoard;
import cn.wxiach.ui.common.layout.Layouts;
import cn.wxiach.ui.settings.LevelRadios;
import cn.wxiach.ui.settings.StoneRadios;
import cn.wxiach.ui.time.GomokuTimeBoard;

import javax.swing.*;

public class GomokuWindow extends JFrame {

    @SuppressWarnings("unused")
    GomokuFlow gomokuFlow = new GomokuFlow();

    public GomokuWindow() {
        // Gomoku action panel, including start, surrender, and revert buttons
        JPanel action = Layouts.container(BoxLayout.Y_AXIS, 12, new StartGameButton(), new SurrenderButton(), new RevertButton());
        action.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));

        // Gomoku preferences panel, including stone color and level radios
        JPanel preferences = Layouts.container(BoxLayout.Y_AXIS, 12, new StoneRadios(), new LevelRadios());
        preferences.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));

        // Compose action and preferences panels into a sidebar
        JPanel sideBar = Layouts.container(BoxLayout.Y_AXIS, 28, new GomokuTimeBoard(), action, new JSeparator(), preferences);
        sideBar.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Compose the main window layout with the Gomoku board and sidebar
        add(Layouts.container(BoxLayout.X_AXIS, 0, new GomokuBoard(), sideBar));
    }
}