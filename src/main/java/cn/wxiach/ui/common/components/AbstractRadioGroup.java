package cn.wxiach.ui.common.components;

import cn.wxiach.event.EventBusAware;
import cn.wxiach.event.GomokuEvent;
import cn.wxiach.event.SubscriberPriority;
import cn.wxiach.event.types.GameOverEvent;
import cn.wxiach.event.types.GameStartEvent;
import cn.wxiach.utils.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于枚举类型的JRadioButton分组抽象基类，
 * 要求枚举类型实现text()方法用于显示文本。
 * 自动处理按钮创建、选中状态和事件发布。
 *
 * @param <T> 枚举类型（必须有text()方法）。
 */
public abstract class AbstractRadioGroup<T extends Enum<T>> extends JPanel implements EventBusAware {

    private final JLabel radioGroupTitle = new JLabel();
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final Map<AbstractButton, T> buttonValueMap = new HashMap<>();

    /**
     * 构造单选按钮组并用指定的枚举选项初始化。
     *
     * @param title   分组标题文本。
     * @param choices 要创建按钮的枚举常量。
     */
    @SafeVarargs
    public AbstractRadioGroup(String title, T... choices) {
        setupRadioGroupTitle(title);
        if (choices != null && choices.length > 0) {
            initializeButtons(choices);
        } else {
            Log.warn("No choices provided for '{}'. No buttons will be created.", title);
        }
        setupLayout(title);
        setupListeners();
        subscribeToEvents();
    }

    private void setupRadioGroupTitle(String title) {
        radioGroupTitle.setText(title);
        add(radioGroupTitle);
        add(Box.createVerticalStrut(8));
    }


    private void setupLayout(String title) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    /**
     * 为指定的枚举选项创建JRadioButton。
     * 要求每个选项的枚举类型有public String text()方法。
     *
     * @param choices 要创建按钮的具体枚举常量。
     * @throws RuntimeException 如果缺少text()方法或调用失败。
     */
    @SafeVarargs
    private void initializeButtons(T... choices) {
        if (choices == null || choices.length == 0) {
            return; // No choices provided
        }

        try {
            // Ensure the text() method exists and returns a String
            java.lang.reflect.Method textMethod = choices[0].getClass().getMethod("text");
            if (!String.class.isAssignableFrom(textMethod.getReturnType())) {
                throw new NoSuchMethodException("text() method does not return a String.");
            }

            for (T enumValue : choices) {
                String displayText = (String) textMethod.invoke(enumValue);
                if (displayText == null || displayText.trim().isEmpty()) {
                    Log.warn("text() method returned null or empty for {}. Skipping button creation.", enumValue);
                    continue;
                }

                // Create and add the button
                JRadioButton button = new JRadioButton(displayText);
                buttonValueMap.put(button, enumValue);
                buttonGroup.add(button);
                add(button);
                add(Box.createVerticalStrut(4));
            }
        } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            throw new RuntimeException("Error processing enum choices.", e);
        }
    }


    /**
     * 创建选中变化时要发布的具体GomokuEvent。
     * 由子类决定具体事件类型。
     *
     * @param selectedValue 新选中的枚举值。
     * @return 选中变化对应的GomokuEvent。
     */
    protected abstract GomokuEvent createSelectEvent(T selectedValue);

    /**
     * 为每个按钮添加ActionListener，通过事件总线发布选中事件。
     */
    private void setupListeners() {
        ActionListener listener = e -> {
            JRadioButton selectedButton = (JRadioButton) e.getSource();
            if (selectedButton.isSelected()) {
                T selectedValue = buttonValueMap.get(selectedButton);
                // Publish the specific event created by the subclass
                publish(createSelectEvent(selectedValue));
            }
        };
        for (AbstractButton button : buttonValueMap.keySet()) {
            button.addActionListener(listener);
        }
    }


    /**
     * 订阅相关事件，控制按钮组的启用/禁用。
     */
    private void subscribeToEvents() {
        subscribe(GameStartEvent.class, event -> setRadioButtonsEnabled(false));
        subscribe(GameOverEvent.class, event -> setRadioButtonsEnabled(true));
        subscribeToCustomEvents();
    }

    protected void subscribeToCustomEvents() {
        // 子类可重写此方法以订阅额外事件
    }

    /**
     * 启用或禁用分组内所有单选按钮和标题标签。
     *
     * @param enabled true启用，false禁用。
     */
    protected void setRadioButtonsEnabled(boolean enabled) {
        radioGroupTitle.setEnabled(enabled);
        for (AbstractButton button : buttonValueMap.keySet()) {
            button.setEnabled(enabled);
        }
    }

    /**
     * 以编程方式选中对应值的单选按钮。
     * 如果值为null或未找到对应按钮则无操作。
     *
     * @param valueToSelect 要选中的枚举值。
     */
    public void setSelectedValue(T valueToSelect) {
        if (valueToSelect == null) {
            return;
        }
        for (Map.Entry<AbstractButton, T> entry : buttonValueMap.entrySet()) {
            if (valueToSelect.equals(entry.getValue())) {
                if (!entry.getKey().isSelected()) {
                    // There can't use setSelected() method, because it will not trigger the ActionListener
                    entry.getKey().doClick();
                }
                return;
            }
        }
        Log.warn("Value {} not found in radio group '{}'. Cannot set selection.", valueToSelect, radioGroupTitle.getText());
    }

    @Override
    public SubscriberPriority defaultSubscriberPriority() {
        return SubscriberPriority.UI;
    }
}