package cn.wxiach.ui.components;

import cn.wxiach.event.EventBusAware;
import cn.wxiach.event.GomokuEvent;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.ui.support.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for radio button groups used for game settings.
 * Provides common functionality for managing a group of radio buttons,
 * handling selection changes, and enabling/disabling based on game state.
 *
 * @param <T> The type of value associated with each radio button (e.g., Color, Level).
 */
public abstract class AbstractRadioGroup<T> extends JPanel implements EventBusAware {

    private final JLabel radioGroupTitle = new JLabel();
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final Map<AbstractButton, T> buttonValueMap = new HashMap<>();

    /**
     * Constructs an AbstractRadioGroup with a title.
     *
     * @param title The title text for the radio group.
     */
    public AbstractRadioGroup(String title) {
        setupLayout(title);
        initializeButtons(buttonGroup);
        selectDefaultButton();
        setupListener();
        subscribeToEvents();
    }

    /**
     * Sets up the basic layout for the panel and applies a TitledBorder.
     *
     * @param title The title text for the border.
     */
    private void setupLayout(String title) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        radioGroupTitle.setText(title);
        Components.createComponentWithMargin(this, radioGroupTitle, 0, 6);
    }

    /**
     * Subclasses must implement this method to create and add their specific
     * JRadioButtons to the panel and the ButtonGroup.
     * Use the {@link #addButton(JRadioButton, Object, ButtonGroup)} helper method.
     *
     * @param group The ButtonGroup to add the radio buttons to.
     */
    protected abstract void initializeButtons(ButtonGroup group);

    /**
     * Subclasses must implement this method to return the default value
     * that should be selected initially.
     *
     * @return The default value.
     */
    protected abstract T getDefaultValue();

    /**
     * Subclasses must implement this method to create the specific event
     * object to be published when the selection changes.
     *
     * @param selectedValue The newly selected value.
     * @return The GomokuEvent representing the selection change.
     */
    protected abstract GomokuEvent createSelectEvent(T selectedValue);

    /**
     * Adds a radio button to the panel, associates it with a value, and adds it to the group.
     *
     * @param button The JRadioButton to add.
     * @param value  The value associated with this button.
     * @param group  The ButtonGroup to add the button to.
     */
    protected void addButton(JRadioButton button, T value, ButtonGroup group) {
        buttonValueMap.put(button, value);
        group.add(button);
        add(button); // Add button to the JPanel
    }

    /**
     * Selects the radio button corresponding to the default value.
     */
    private void selectDefaultButton() {
        T defaultValue = getDefaultValue();
        for (Map.Entry<AbstractButton, T> entry : buttonValueMap.entrySet()) {
            if (entry.getValue().equals(defaultValue)) {
                entry.getKey().setSelected(true);
                break;
            }
        }
    }

    /**
     * Adds action listeners to all radio buttons in the group.
     * When a button is selected, it publishes the appropriate selection event.
     */
    private void setupListener() {
        ActionListener listener = e -> {
            JRadioButton selectedButton = (JRadioButton) e.getSource();
            if (selectedButton.isSelected()) {
                T selectedValue = buttonValueMap.get(selectedButton);
                publish(createSelectEvent(selectedValue));
            }
        };
        for (AbstractButton button : buttonValueMap.keySet()) {
            button.addActionListener(listener);
        }
    }

    /**
     * Subscribes to Gomoku events (GameStartEvent, GameOverEvent) to manage
     * the enabled state of the radio buttons.
     */
    private void subscribeToEvents() {
        subscribe(GameStartEvent.class, event -> setRadioButtonsEnabled(false));
        subscribe(GameOverEvent.class, event -> setRadioButtonsEnabled(true));
    }

    /**
     * Enables or disables all radio buttons in the group.
     *
     * @param enabled true to enable, false to disable.
     */
    protected void setRadioButtonsEnabled(boolean enabled) {
        radioGroupTitle.setEnabled(enabled);
        for (AbstractButton button : buttonValueMap.keySet()) {
            button.setEnabled(enabled);
        }
    }

    /**
     * Gets the currently selected value from the radio button group.
     *
     * @return The value associated with the currently selected radio button.
     */
    public T getCurrentValue() {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return buttonValueMap.get(button);
            }
        }
        return null; // Should not happen if a button is always selected
    }
}