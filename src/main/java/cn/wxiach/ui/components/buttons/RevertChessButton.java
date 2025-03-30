package cn.wxiach.ui.components.buttons;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RevertChessButton extends JButton {

    public RevertChessButton() {
        setText("悔棋");
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
        setEnabled(false);

        addActionListeners();
        subscribeToEvents();
    }

    private void addActionListeners() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("revert chess");
            }
        });
    }


    private void subscribeToEvents() {
        GomokuEventBus.getInstance().subscribe(GameStartEvent.class, event -> {
            setEnabled(true);
        });


        GomokuEventBus.getInstance().subscribe(GameOverEvent.class, event -> {
            setEnabled(false);
        });
    }
}
