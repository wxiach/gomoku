package cn.wxiach.ui.components.buttons;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGameButton extends JButton {

    private static final Logger logger = LoggerFactory.getLogger(StartGameButton.class);

    public StartGameButton() {
        setText("开始游戏");
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
        addActionListener();
        subscribeToEvents();
    }

    private void addActionListener() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Game start.");
                setEnabled(false);
                GomokuEventBus.getInstance().publish(new GameStartEvent(this));
            }
        });
    }

    private void subscribeToEvents() {
        GomokuEventBus.getInstance().subscribe(GameOverEvent.class, event -> {
            setEnabled(true);
        });
    }
}
