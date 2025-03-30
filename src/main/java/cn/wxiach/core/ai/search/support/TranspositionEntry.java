package cn.wxiach.core.ai.search.support;

import cn.wxiach.model.Color;

public record TranspositionEntry(int evaluation, int evaluationType, int depth, Color color) {
    public static final int EXACT = 0;
    public static final int LOWER_BOUND = 1;
    public static final int UPPER_BOUND = 2;
}
