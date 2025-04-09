package cn.wxiach.ui.components;

import cn.wxiach.core.config.GomokuConf;
import cn.wxiach.core.model.Color;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.event.support.StoneSelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StoneRadio extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(StoneRadio.class);

    private final JLabel radioTitle;
    private final JRadioButton blackButton;
    private final JRadioButton whiteButton;

    public StoneRadio() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        blackButton = new JRadioButton("黑棋（先手）");
        whiteButton = new JRadioButton("白棋（后手）");

        // Add the button to the ButtonGroup, ensure that only one can be selected
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(blackButton);
        buttonGroup.add(whiteButton);

        // Set default choose button
        if (GomokuConf.DEFAULT_SEL_COLOR == Color.BLACK) {
            blackButton.setSelected(true);
        } else {
            whiteButton.setSelected(true);
        }

        radioTitle = new JLabel("棋子颜色：");
        add(radioTitle);


        add(Box.createVerticalStrut(12));

        add(blackButton);
        add(whiteButton);

        setVisible(true);

        addActionListeners();

        subscribeToEvents();
    }

    private void addActionListeners() {
        blackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("You select black stone.");
                GomokuEventBus.getInstance().publish(new StoneSelectEvent(this, Color.BLACK));
            }
        });

        whiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("You select white stone.");
                GomokuEventBus.getInstance().publish(new StoneSelectEvent(this, Color.WHITE));
            }
        });
    }

    private void subscribeToEvents() {
        GomokuEventBus.getInstance().subscribe(GameStartEvent.class, event -> {
            radioTitle.setEnabled(false);
            blackButton.setEnabled(false);
            whiteButton.setEnabled(false);
        });


        GomokuEventBus.getInstance().subscribe(GameOverEvent.class, event -> {
            radioTitle.setEnabled(true);
            blackButton.setEnabled(true);
            whiteButton.setEnabled(true);
        });
    }

    public Color getCurrentValue() {
        return blackButton.isSelected() ? Color.BLACK : Color.WHITE;
    }
}
