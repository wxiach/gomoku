package cn.wxiach.ui.settings;

import cn.wxiach.event.GomokuEvent;
import cn.wxiach.event.types.PreferencesLoadedEvent;
import cn.wxiach.event.types.StoneSelectEvent;
import cn.wxiach.model.Color;
import cn.wxiach.ui.common.components.AbstractRadioGroup;

/**
 * 棋子颜色选择单选按钮组（黑棋或白棋）。
 * 初始化时有默认值，并会根据ConfigurationLoadedEvent进行更新。
 */
public class StoneRadios extends AbstractRadioGroup<Color> {

    public StoneRadios() {
        super("棋子颜色", Color.BLACK, Color.WHITE);
    }

    @Override
    protected void subscribeToCustomEvents() {
        // 订阅PreferencesLoadedEvent以设置初始选择
        subscribe(PreferencesLoadedEvent.class, event -> setSelectedValue(event.getPreferences().color()));
    }

    /**
     * 当颜色选择变化时创建StoneSelectEvent。
     *
     * @param selectedValue 新选择的棋子颜色。
     * @return 新的StoneSelectEvent。
     */
    @Override
    protected GomokuEvent createSelectEvent(Color selectedValue) {
        return new StoneSelectEvent(this, selectedValue);
    }
}
