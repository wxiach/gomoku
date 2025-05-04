package cn.wxiach.event.types;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Preferences;

/**
 * @author wxiach 2025/5/3
 */
public class PreferencesLoadedEvent extends GomokuEvent {

    private final Preferences preferences;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public PreferencesLoadedEvent(Object source, Preferences preferences) {
        super(source);
        this.preferences = preferences;
    }

    public Preferences getPreferences() {
        return preferences;
    }
}
