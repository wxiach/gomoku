package cn.wxiach.model;

/**
 * Manages game settings and preferences.
 *
 * @param level Game difficulty level {@link Level}.
 * @param color Human player's stone color {@link Color}.
 * @author wxiach 2025/5/2
 */
public record Preferences(Level level, Color color) {
}
