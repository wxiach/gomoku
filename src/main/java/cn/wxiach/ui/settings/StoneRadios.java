package cn.wxiach.ui.settings;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.event.support.PreferencesLoadedEvent;
import cn.wxiach.event.support.StoneSelectEvent;
import cn.wxiach.model.Color;
import cn.wxiach.ui.common.components.AbstractRadioGroup;

/**
 * Radio button group for selecting the player's stone color (Black or White).
 * Initializes with a default value and updates based on ConfigurationLoadedEvent.
 */
public class StoneRadios extends AbstractRadioGroup<Color> {

    public StoneRadios() {
        super("棋子颜色", Color.BLACK, Color.WHITE);
    }

    @Override
    protected void subscribeToCustomEvents() {
        // Subscribe to PreferencesLoadedEvent to set the initial selection
        subscribe(PreferencesLoadedEvent.class, event -> setSelectedValue(event.getPreferences().color()));
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
