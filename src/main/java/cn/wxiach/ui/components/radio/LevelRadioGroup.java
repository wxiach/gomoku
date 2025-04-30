package cn.wxiach.ui.components.radio;

import cn.wxiach.core.config.GomokuConf;
import cn.wxiach.core.model.Level;
import cn.wxiach.event.GomokuEvent;
import cn.wxiach.event.support.LevelSelectEvent;
import cn.wxiach.ui.components.AbstractRadioGroup;

import javax.swing.*;

/**
 * Radio button group for selecting the game difficulty level.
 * Extends AbstractRadioGroup to inherit common radio group functionality.
 */
public class LevelRadioGroup extends AbstractRadioGroup<Level> {

    public LevelRadioGroup() {
        super("难度");
    }

    /**
     * Initializes the radio buttons for different difficulty levels.
     *
     * @param group The ButtonGroup to add the buttons to.
     */
    @Override
    protected void initializeButtons(ButtonGroup group) {
        addButton(new JRadioButton("困难（职业）"), Level.DIFFICULT, group);
        addButton(new JRadioButton("一般（业余）"), Level.NORMAL, group);
        addButton(new JRadioButton("简单（初学者）"), Level.EASY, group);
    }

    /**
     * Returns the default difficulty level specified in GomokuConf.
     *
     * @return The default Level.
     */
    @Override
    protected Level getDefaultValue() {
        return GomokuConf.DEFAULT_LEVEL;
    }

    /**
     * Creates a LevelSelectEvent when the difficulty level selection changes.
     *
     * @param selectedValue The newly selected Level.
     * @return A new LevelSelectEvent.
     */
    @Override
    protected GomokuEvent createSelectEvent(Level selectedValue) {
        return new LevelSelectEvent(this, selectedValue);
    }
}
