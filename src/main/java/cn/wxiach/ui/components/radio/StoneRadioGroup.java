package cn.wxiach.ui.components.radio;

import cn.wxiach.core.config.GomokuConf;
import cn.wxiach.core.model.Color;
import cn.wxiach.event.GomokuEvent;
import cn.wxiach.event.support.StoneSelectEvent;
import cn.wxiach.ui.components.AbstractRadioGroup;

import javax.swing.*;

/**
 * Radio button group for selecting the player's stone color (Black or White).
 * Extends AbstractRadioGroup to inherit common radio group functionality.
 */
public class StoneRadioGroup extends AbstractRadioGroup<Color> {

    public StoneRadioGroup() {
        super("棋子颜色");
    }

    /**
     * Initializes the Black and White radio buttons.
     *
     * @param group The ButtonGroup to add the buttons to.
     */
    @Override
    protected void initializeButtons(ButtonGroup group) {
        addButton(new JRadioButton("黑棋（先手）"), Color.BLACK, group);
        addButton(new JRadioButton("白棋（后手）"), Color.WHITE, group);
    }

    /**
     * Returns the default stone color specified in GomokuConf.
     *
     * @return The default Color.
     */
    @Override
    protected Color getDefaultValue() {
        return GomokuConf.DEFAULT_SEL_COLOR;
    }

    /**
     * Creates a StoneSelectEvent when the color selection changes.
     *
     * @param selectedValue The newly selected Color.
     * @return A new StoneSelectEvent.
     */
    @Override
    protected GomokuEvent createSelectEvent(Color selectedValue) {
        return new StoneSelectEvent(this, selectedValue);
    }
}
