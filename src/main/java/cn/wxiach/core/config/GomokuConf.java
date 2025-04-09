package cn.wxiach.core.config;

import cn.wxiach.core.model.Color;
import cn.wxiach.core.model.Level;

/**
 * GomokuConf
 * <p>
 * This class contains the default configuration and settings key names
 * for the Gomoku game.
 */
public class GomokuConf {

    // ==================================================
    // Default Configuration
    // ==================================================

    /**
     * Default selected stone color
     */
    public static final Color DEFAULT_SEL_COLOR = Color.BLACK;

    /**
     * Default game level
     */
    public static final Level DEFAULT_LEVEL = Level.NORMAL;

    // ==================================================
    // Settings Key Names
    // ==================================================

    /**
     * Key for self stone color setting
     */
    public static final String SELF_STONE_COLOR = "color";

    /**
     * Key for game level setting
     */
    public static final String LEVEL = "level";

}
