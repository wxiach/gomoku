package cn.wxiach.ui.components;

import cn.wxiach.config.GomokuConf;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.DifficultSelectEvent;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.model.Difficult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DifficultRadio extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(DifficultRadio.class);

    private final JLabel radioTitle;
    private final JRadioButton difficultButton;
    private final JRadioButton normalButton;
    private final JRadioButton easyButton;

    public DifficultRadio() {
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
        if (GomokuConf.defaultDifficult == Difficult.DIFFICULT) {
            difficultButton.setSelected(true);
        } else if (GomokuConf.defaultDifficult == Difficult.EASY) {
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
                GomokuEventBus.getInstance().publish(new DifficultSelectEvent(this, Difficult.DIFFICULT));
            }
        });

        normalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("normal is selected");
                GomokuEventBus.getInstance().publish(new DifficultSelectEvent(this, Difficult.NORMAL));
            }
        });

        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("easy is selected");
                GomokuEventBus.getInstance().publish(new DifficultSelectEvent(this, Difficult.EASY));
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

    public Difficult getCurrentValue() {
        if (difficultButton.isSelected()) return Difficult.DIFFICULT;
        if (normalButton.isSelected()) return Difficult.NORMAL;
        return Difficult.EASY;
    }
}
