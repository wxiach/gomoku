package cn.wxiach.ui.components.buttons;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.event.support.PieceSelectEvent;
import cn.wxiach.model.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PieceColorSelectButton extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(PieceColorSelectButton.class);

    private final JLabel radioTitle;
    private final JRadioButton blackButton;
    private final JRadioButton whiteButton;

    public PieceColorSelectButton() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        radioTitle = new JLabel("选择你的棋子颜色：");
        add(radioTitle);

        blackButton = new JRadioButton("黑棋（先手）");
        whiteButton = new JRadioButton("白棋（后手）");

        // Add the button to the ButtonGroup, ensure that only one can be selected
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(blackButton);
        buttonGroup.add(whiteButton);

        // By default, black is chosen
        blackButton.setSelected(true);

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
                logger.debug("You select black piece.");
                GomokuEventBus.getInstance().publish(new PieceSelectEvent(this, Color.BLACK));
            }
        });

        whiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("You select white piece.");
                GomokuEventBus.getInstance().publish(new PieceSelectEvent(this, Color.WHITE));
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
