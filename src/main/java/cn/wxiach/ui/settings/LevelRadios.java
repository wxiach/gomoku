package cn.wxiach.ui.settings;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.event.types.LevelSelectEvent;
import cn.wxiach.event.types.PreferencesLoadedEvent;
import cn.wxiach.model.Level;
import cn.wxiach.ui.common.components.AbstractRadioGroup;

/**
 * Radio button group for selecting the game difficulty level.
 * Initializes with a default value and updates based on PreferencesLoadedEvent.
 */
public class LevelRadios extends AbstractRadioGroup<Level> {

    public LevelRadios() {
        super("难度", Level.EASY, Level.MEDIUM, Level.HARD);
    }

    @Override
    protected void subscribeToCustomEvents() {
        // Subscribe to PreferencesLoadedEvent to set the initial selection
        subscribe(PreferencesLoadedEvent.class, event -> setSelectedValue(event.getPreferences().level()));
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
