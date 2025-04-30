package cn.wxiach.ui;

import cn.wxiach.ui.components.TimeBoardGroup;
import cn.wxiach.ui.components.buttons.RevertButton;
import cn.wxiach.ui.components.buttons.StartGameButton;
import cn.wxiach.ui.components.buttons.SurrenderButton;
import cn.wxiach.ui.components.radio.LevelRadioGroup;
import cn.wxiach.ui.components.radio.StoneRadioGroup;
import cn.wxiach.ui.support.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ControlPanel extends JPanel {

    private static final int PANEL_WIDTH = 200;

    private final StoneRadioGroup stoneRadio = new StoneRadioGroup();
    private final LevelRadioGroup levelRadio = new LevelRadioGroup();

    public ControlPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(24, 16, 24, 16));

        Components.createComponentWithMargin(this, new TimeBoardGroup(), 0, 24);
        Components.createComponentWithMargin(this, new StartGameButton(), 0, 24);
        Components.createComponentWithMargin(this, new SurrenderButton(), 0, 24);
        Components.createComponentWithMargin(this, new RevertButton(), 0, 24);
        Components.createComponentWithMargin(this, Components.createHorizontalSeparator(), 24, 24);
        Components.createComponentWithMargin(this, stoneRadio, 0, 24);
        Components.createComponentWithMargin(this, levelRadio, 0, 24);
    }

    public StoneRadioGroup getStoneColorSelectButton() {
        return this.stoneRadio;
    }

    public LevelRadioGroup getLevelRadio() {
        return this.levelRadio;
    }

}
