package cn.wxiach.config;

import cn.wxiach.model.Color;
import cn.wxiach.model.Difficult;

public class GomokuConf {

    // ==================================================
    // Default config
    // ==================================================
    public static final Color defaultSelColor = Color.BLACK;

    public static final Difficult defaultDifficult = Difficult.NORMAL;

    // ==================================================
    // Settings key name
    // ==================================================

    public static final String SELF_STONE_COLOR = "selfStoneColor";

    public static final String DIFFICULT = "difficult";

}
