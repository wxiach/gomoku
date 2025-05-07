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

            // Set as the system's native Look and Feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                Log.error("Failed to set system look and feel", e);
            }

            // Set the default font for the application
            FontAssets.setGlobalFont(FontAssets.LXGWWenKaiMonoScreen);

            // Create a new Gomoku window
            GomokuWindow window = new GomokuWindow();

            // Set the window properties
            window.setTitle("五子棋 - 与 AI 对战");
            window.setIconImage(ImageAssets.getLogoImage());

            // Set the default close operation for the window
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            // Set the size of the window
            window.pack();

            // Set the window to be non-resizable
            window.setResizable(false);

            // Center the window on the screen
            window.setLocationRelativeTo(null);

            // Set the window to be visible
            window.setVisible(true);

            // Load the default Gomoku preferences
            Preferences preferences = new Preferences(Level.EASY, Color.BLACK);
            GomokuEventBus.getInstance().publish(new PreferencesLoadedEvent(Gomoku.class, preferences));
        });
    }

}