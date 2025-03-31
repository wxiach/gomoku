package cn.wxiach.ui.components;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.event.support.NewTurnEvent;
import cn.wxiach.model.Color;
import cn.wxiach.ui.ComponentUtils;
import cn.wxiach.ui.assets.FontAssets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeBoardPanel extends JPanel {

    private static final int PANEL_WIDTH = 300;

    private final Timer blackTimer;
    private final Timer whiteTimer;

    private int blackTime = 0;
    private int whiteTime = 0;

    private static final JLabel blackTimerLabel = new JLabel("00:00");
    private static final JLabel whiteTimerLabel = new JLabel("00:00");

    public TimeBoardPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(PANEL_WIDTH, 80));

        JLabel blackTitle = new JLabel("黑棋用时：");
        blackTimerLabel.setFont(FontAssets.LXGWWenKaiMonoScreen.deriveFont(Font.BOLD, 28f));
        add(ComponentUtils.createHorizontalContainer(blackTitle, blackTimerLabel));

        JLabel whiteTitle = new JLabel("白棋用时：");
        whiteTimerLabel.setFont(FontAssets.LXGWWenKaiMonoScreen.deriveFont(Font.BOLD, 28f));
        add(ComponentUtils.createHorizontalContainer(whiteTitle, whiteTimerLabel));


        blackTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blackTimerLabel.setText(formatTime(++blackTime));
            }
        });

        whiteTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                whiteTimerLabel.setText(formatTime(++whiteTime));
            }
        });

        subscribeToEvents();
    }

    private void subscribeToEvents() {

        GomokuEventBus.getInstance().subscribe(GameStartEvent.class, event -> {
            blackTime = 0;
            whiteTime = 0;
        });

        GomokuEventBus.getInstance().subscribe(NewTurnEvent.class, event -> {
            if (event.getCurrentTurn() == Color.BLACK) {
                blackTimer.start();
                whiteTimer.stop();
            } else {
                whiteTimer.start();
                blackTimer.stop();
            }
        });

        GomokuEventBus.getInstance().subscribe(GameOverEvent.class, event -> {
            blackTimer.stop();
            blackTimer.stop();
        });
    }

    private String formatTime(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}
