package cn.wxiach.ui.time;

import cn.wxiach.event.EventBusAware;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.event.support.NewTurnEvent;
import cn.wxiach.model.Color;
import cn.wxiach.ui.common.assets.FontAssets;
import cn.wxiach.ui.common.layout.Layouts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GomokuTimeBoard extends JPanel implements EventBusAware {

    private static final float TIMER_FONT_SIZE = 34f;
    private static final int TIMER_DELAY = 1000; // milliseconds

    private final TimeBoard blackTimeBoard = new TimeBoard("黑棋");
    private final TimeBoard whiteTimeBoard = new TimeBoard("白棋");

    public GomokuTimeBoard() {
        setupLayout();
        subscribeToEvents();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(Layouts.container(BoxLayout.Y_AXIS, 0, blackTimeBoard, whiteTimeBoard));
    }

    private void subscribeToEvents() {
        subscribe(GameStartEvent.class, event -> {
            blackTimeBoard.reset();
            whiteTimeBoard.reset();
            blackTimeBoard.start();
        });

        subscribe(NewTurnEvent.class, event -> {
            if (event.getTurn().currentTurn() == Color.BLACK) {
                blackTimeBoard.start();
                whiteTimeBoard.stop();
            } else {
                whiteTimeBoard.start();
                blackTimeBoard.stop();
            }
        });

        subscribe(GameOverEvent.class, event -> {
            blackTimeBoard.stop();
            whiteTimeBoard.stop();
        });
    }

    private static class TimeBoard extends JPanel {
        private final JLabel label;
        private final Timer timer;
        private int timeSeconds = 0;

        TimeBoard(String titleText) {
            this.label = new JLabel(formatTime(timeSeconds));
            this.label.setFont(FontAssets.LXGWWenKaiMonoScreen.deriveFont(Font.BOLD, TIMER_FONT_SIZE));

            this.timer = new Timer(TIMER_DELAY, e -> {
                timeSeconds++;
                updateLabel();
            });

            Border lineBorder = BorderFactory.createLineBorder(java.awt.Color.GRAY, 2);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(lineBorder, titleText);
            titledBorder.setTitleFont(FontAssets.LXGWWenKaiMonoScreen);

            Border withPadding = BorderFactory.createCompoundBorder(
                    titledBorder,
                    BorderFactory.createEmptyBorder(0, 12, 0, 12)
            );

            this.label.setBorder(withPadding);

            add(label);
        }

        void start() {
            timer.start();
        }

        void stop() {
            timer.stop();
        }

        void reset() {
            timer.stop();
            timeSeconds = 0;
            updateLabel();
        }

        void updateLabel() {
            label.setText(formatTime(timeSeconds));
        }

        private static String formatTime(int seconds) {
            return String.format("%02d:%02d", seconds / 60, seconds % 60);
        }
    }
}
