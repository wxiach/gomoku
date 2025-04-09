package cn.wxiach.ui;

import cn.wxiach.ui.components.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ControlPanel extends JPanel {

    private static final int PANEL_WIDTH = 200;

    private final StoneRadio stoneRadio;
    private final LevelRadio levelRadio;

    public ControlPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(24, 16, 24, 16));

        add(new TimeBoardPanel());
        add(Box.createVerticalStrut(24));

        add(new StartGameButton());
        add(Box.createVerticalStrut(24));

        add(new SurrenderButton());
        add(Box.createVerticalStrut(24));

        add(new RevertButton());
        add(Box.createVerticalStrut(48));

        add(ComponentUtils.createHorizontalSeparator());

        add(Box.createVerticalStrut(24));
        stoneRadio = new StoneRadio();
        add(stoneRadio);
        add(Box.createVerticalStrut(24));

        levelRadio = new LevelRadio();
        add(levelRadio);
    }

    public StoneRadio getStoneColorSelectButton() {
        return this.stoneRadio;
    }

    public LevelRadio getLevelRadio() {
        return this.levelRadio;
    }

}
