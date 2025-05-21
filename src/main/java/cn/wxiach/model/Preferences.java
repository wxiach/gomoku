package cn.wxiach.model;

/**
 * Manages game settings and preferences.
 * 管理游戏设置和偏好。
 *
 * @param level 游戏难度 {@link Level}.
 * @param color 玩家棋子颜色 {@link Color}.
 * @author wxiach 2025/5/2
 */
public record Preferences(Level level, Color color) {
}
