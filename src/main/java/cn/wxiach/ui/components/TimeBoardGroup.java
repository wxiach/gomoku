package cn.wxiach.ui.components;

import cn.wxiach.core.model.Color;
import cn.wxiach.event.EventBusAware;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.event.support.NewTurnEvent;
import cn.wxiach.ui.assets.FontAssets;
import cn.wxiach.ui.support.Components;

import javax.swing.*;
import java.awt.*;

public class TimeBoardGroup extends JPanel implements EventBusAware {

    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 80;
    private static final float TIMER_FONT_SIZE = 28f;
    private static final int TIMER_DELAY = 1000; // milliseconds

    private TimeBoard blackTimeBoard;
    private TimeBoard whiteTimeBoard;

    public TimeBoardGroup() {
        setupLayout();
        initializeComponents();
        subscribeToEvents();
    }

    private void setupLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    }

    private void initializeComponents() {
        blackTimeBoard = createTimeBoard("黑棋用时:");
        whiteTimeBoard = createTimeBoard("白棋用时:");
    }

    private TimeBoard createTimeBoard(String title) {
        TimeBoard timeBoard = new TimeBoard(title);
        add(timeBoard);
        return timeBoard;
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

            JLabel titleLabel = new JLabel(titleText);
            JPanel contentPanel = Components.createHorizontalContainer(titleLabel, this.label);

            setLayout(new BorderLayout());
            add(contentPanel, BorderLayout.CENTER);
            setOpaque(false);
        }

        void start() {
            timer.start();
        }

        void stop() {
            timer.stop();
        }

        void reset() {
            stop();
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
