package cn.wxiach.event.types;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Preferences;

/**
 * @author wxiach 2025/5/3
 */
public class PreferencesLoadedEvent extends GomokuEvent {

    private final Preferences preferences;

    public PreferencesLoadedEvent(Object source, Preferences preferences) {
        super(source);
        this.preferences = preferences;
    }

    public Preferences getPreferences() {
        return preferences;
    }
}
