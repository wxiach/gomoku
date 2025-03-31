package cn.wxiach.ui.components;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.event.support.NewTurnEvent;
import cn.wxiach.event.support.RevertChessEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RevertButton extends JButton {

    public RevertButton() {
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
                GomokuEventBus.getInstance().publish(new RevertChessEvent(this));
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

        GomokuEventBus.getInstance().subscribe(NewTurnEvent.class, event -> {
            setEnabled(event.getTurn().isSelfTurn());
        });

    }
}
