package cn.wxiach.ui.components;

import cn.wxiach.config.GomokuConf;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.event.support.LevelSelectEvent;
import cn.wxiach.model.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelRadio extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(LevelRadio.class);

    private final JLabel radioTitle;
    private final JRadioButton difficultButton;
    private final JRadioButton normalButton;
    private final JRadioButton easyButton;

    public LevelRadio() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        difficultButton = new JRadioButton("困难（职业）");
        normalButton = new JRadioButton("一般（业余）");
        easyButton = new JRadioButton("简单（初学者）");

        // Add the button to the ButtonGroup, ensure that only one can be selected
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(difficultButton);
        buttonGroup.add(normalButton);
        buttonGroup.add(easyButton);

        // Set default choose button
        if (GomokuConf.DEFAULT_LEVEL == Level.DIFFICULT) {
            difficultButton.setSelected(true);
        } else if (GomokuConf.DEFAULT_LEVEL == Level.EASY) {
            easyButton.setSelected(true);
        } else {
            normalButton.setSelected(true);
        }

        radioTitle = new JLabel("难度：");
        add(radioTitle);

        add(Box.createVerticalStrut(12));

        add(difficultButton);
        add(normalButton);
        add(easyButton);

        setVisible(true);

        addActionListeners();

        subscribeToEvents();
    }

    private void addActionListeners() {
        difficultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("difficult is selected");
                GomokuEventBus.getInstance().publish(new LevelSelectEvent(this, Level.DIFFICULT));
            }
        });

        normalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("normal is selected");
                GomokuEventBus.getInstance().publish(new LevelSelectEvent(this, Level.NORMAL));
            }
        });

        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("easy is selected");
                GomokuEventBus.getInstance().publish(new LevelSelectEvent(this, Level.EASY));
            }
        });
    }

    private void subscribeToEvents() {
        GomokuEventBus.getInstance().subscribe(GameStartEvent.class, event -> {
            radioTitle.setEnabled(false);
            difficultButton.setEnabled(false);
            normalButton.setEnabled(false);
            easyButton.setEnabled(false);
        });


        GomokuEventBus.getInstance().subscribe(GameOverEvent.class, event -> {
            radioTitle.setEnabled(true);
            difficultButton.setEnabled(true);
            normalButton.setEnabled(true);
            easyButton.setEnabled(true);
        });
    }

    public Level getCurrentValue() {
        if (difficultButton.isSelected()) return Level.DIFFICULT;
        if (normalButton.isSelected()) return Level.NORMAL;
        return Level.EASY;
    }
}
