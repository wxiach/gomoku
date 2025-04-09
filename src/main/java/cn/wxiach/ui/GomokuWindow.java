package cn.wxiach.ui;


import cn.wxiach.config.GomokuConf;
import cn.wxiach.core.GameFlow;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.model.Color;
import cn.wxiach.model.Level;
import cn.wxiach.ui.assets.FontAssets;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GomokuWindow extends JFrame {

    private final GameFlow gameFlow = new GameFlow();

    private final BoardPanel boardPanel = new BoardPanel();
    private final ControlPanel controlPanel = new ControlPanel();

    public GomokuWindow() throws Exception {
        // Set as the system's native Look and Feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        FontAssets.setGlobalFont(FontAssets.LXGWWenKaiMonoScreen);

        setTitle("五子棋 - 与 AI 对战");

        setLayout(new BorderLayout());
        add(new BoardPanel(), BorderLayout.CENTER);
        add(new ControlPanel(), BorderLayout.EAST);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GomokuEventBus.getInstance().subscribe(GameOverEvent.class, event -> {
        });

        this.setVisible(true);
    }

    public void run() {
        Map<String, Object> config = new HashMap<>();

        Color selfStoneColor = controlPanel.getStoneColorSelectButton().getCurrentValue();
        config.put(GomokuConf.SELF_STONE_COLOR, selfStoneColor);

        Level level = controlPanel.getLevelRadio().getCurrentValue();
        config.put(GomokuConf.LEVEL, level);

        gameFlow.updateGameSettings(config);
    }
}
