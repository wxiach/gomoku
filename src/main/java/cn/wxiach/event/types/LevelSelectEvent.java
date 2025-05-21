package cn.wxiach.event.types;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.model.Level;

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
