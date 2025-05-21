package cn.wxiach.ui.settings;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.event.types.LevelSelectEvent;
import cn.wxiach.event.types.PreferencesLoadedEvent;
import cn.wxiach.model.Level;
import cn.wxiach.ui.common.components.AbstractRadioGroup;

/**
 * 难度选择单选按钮组。
 * 初始化时有默认值，并会根据PreferencesLoadedEvent进行更新。
 */
public class LevelRadios extends AbstractRadioGroup<Level> {

    public LevelRadios() {
        super("难度", Level.EASY, Level.MEDIUM, Level.HARD);
    }

    @Override
    protected void subscribeToCustomEvents() {
        // 订阅PreferencesLoadedEvent以设置初始选择
        subscribe(PreferencesLoadedEvent.class, event -> setSelectedValue(event.getPreferences().level()));
    }

    /**
     * 当难度选择变化时创建LevelSelectEvent。
     *
     * @param selectedValue 新选择的难度等级。
     * @return 新的LevelSelectEvent。
     */
    @Override
    protected GomokuEvent createSelectEvent(Level selectedValue) {
        return new LevelSelectEvent(this, selectedValue);
    }
}
