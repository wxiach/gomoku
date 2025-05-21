package cn.wxiach;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.types.PreferencesLoadedEvent;
import cn.wxiach.model.Color;
import cn.wxiach.model.Level;
import cn.wxiach.model.Preferences;
import cn.wxiach.ui.common.assets.FontAssets;
import cn.wxiach.ui.common.assets.ImageAssets;
import cn.wxiach.ui.window.GomokuWindow;
import cn.wxiach.utils.Log;

import javax.swing.*;

public class Gomoku {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // 设置为系统原生外观
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                Log.error("Failed to set system look and feel", e);
            }

            // 设置应用程序默认字体
            FontAssets.setGlobalFont(FontAssets.LXGWWenKaiMonoScreen);

            // 创建新的五子棋窗口
            GomokuWindow window = new GomokuWindow();

            // 设置窗口属性
            window.setTitle("五子棋 - 与 AI 对战");
            window.setIconImage(ImageAssets.getLogoImage());

            // 设置窗口默认关闭操作
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            // 设置窗口大小
            window.pack();

            // 设置窗口不可调整大小
            window.setResizable(false);

            // 窗口居中显示
            window.setLocationRelativeTo(null);

            // 设置窗口可见
            window.setVisible(true);

            // 加载默认五子棋偏好设置
            Preferences preferences = new Preferences(Level.EASY, Color.BLACK);
            GomokuEventBus.getInstance().publish(new PreferencesLoadedEvent(Gomoku.class, preferences));
        });
    }

}