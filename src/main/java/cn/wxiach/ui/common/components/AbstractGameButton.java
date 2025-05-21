package cn.wxiach.ui.common.components;

import cn.wxiach.event.EventBusAware;
import cn.wxiach.event.SubscriberPriority;

import javax.swing.*;
import java.awt.*;

/**
 * 所有游戏控制按钮的抽象基类。
 * 提供游戏按钮的通用功能，包括：
 * - 按钮文本设置
 * - 事件发布（在handleButtonClick中处理）
 * - 按钮状态管理
 */
public abstract class AbstractGameButton extends JButton implements EventBusAware {

    public AbstractGameButton(String buttonText) {
        this(buttonText, true);
    }

    public AbstractGameButton(String buttonText, boolean enabled) {
        setupLayout(buttonText, enabled);
        setupListeners();
        subscribeToEvents();
    }

    private void setupLayout(String buttonText, boolean enabled) {
        setText(buttonText);
        setPreferredSize(new Dimension(getPreferredSize().width, (int) (getPreferredSize().height * 1.4)));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height * 2));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setEnabled(enabled);
    }

    private void setupListeners() {
        addActionListener(e -> handleButtonClick());
    }

    /**
     * 子类必须实现此方法以定义按钮点击时执行的操作。
     * 这可能包括发布事件。
     */
    protected abstract void handleButtonClick();

    /**
     * 子类必须实现此方法以订阅相关事件，
     * 这些事件可能影响按钮状态（如启用/禁用）。
     */
    protected abstract void subscribeToEvents();

    @Override
    public SubscriberPriority defaultSubscriberPriority() {
        return SubscriberPriority.UI;
    }
}