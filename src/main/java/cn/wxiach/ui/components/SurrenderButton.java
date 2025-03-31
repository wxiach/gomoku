package cn.wxiach.ui.components;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SurrenderButton extends JButton {

    public SurrenderButton() {
        setText("认输");
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
        setEnabled(false);
        addActionListener();
        subscribeToEvents();
    }

    private void subscribeToEvents() {
        GomokuEventBus.getInstance().subscribe(GameStartEvent.class, event -> {
            setEnabled(true);
        });
        GomokuEventBus.getInstance().subscribe(GameOverEvent.class, event -> {
            setEnabled(false);
        });
    }

    private void addActionListener() {
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEnabled(false);
                GomokuEventBus.getInstance().publish(new GameOverEvent(this, cn.wxiach.model.Color.EMPTY));
            }
        });
    }
}