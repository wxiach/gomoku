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
        // 五子棋操作面板，包括开始、认输和悔棋按钮
        JPanel action = Layouts.container(BoxLayout.Y_AXIS, 12, new StartGameButton(), new SurrenderButton(), new RevertButton());
        action.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));

        // 五子棋偏好设置面板，包括棋子颜色和难度单选按钮
        JPanel preferences = Layouts.container(BoxLayout.Y_AXIS, 12, new StoneRadios(), new LevelRadios());
        preferences.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));

        // 组合操作和偏好设置面板为侧边栏
        JPanel sideBar = Layouts.container(BoxLayout.Y_AXIS, 28, new GomokuTimeBoard(), action, new JSeparator(), preferences);
        sideBar.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // 组合主窗口布局，包括五子棋棋盘和侧边栏
        add(Layouts.container(BoxLayout.X_AXIS, 0, new GomokuBoard(), sideBar));
    }
}