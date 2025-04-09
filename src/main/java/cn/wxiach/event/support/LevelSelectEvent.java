package cn.wxiach.event.support;

import cn.wxiach.core.model.Level;
import cn.wxiach.event.GomokuEvent;

public class LevelSelectEvent extends GomokuEvent {
    private final Level level;

    public LevelSelectEvent(Object source, Level level) {
        super(source);
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
