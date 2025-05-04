package cn.wxiach.ui.common.components;

import cn.wxiach.event.EventBusAware;
import cn.wxiach.event.GomokuEvent;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.utils.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for a group of JRadioButtons backed by an Enum type
 * where the Enum provides its own display text via a text() method.
 * Handles automatic button creation, selection state, and event publishing.
 *
 * @param <T> The Enum type (must have a text() method).
 */
public abstract class AbstractRadioGroup<T extends Enum<T>> extends JPanel implements EventBusAware {

    private final JLabel radioGroupTitle = new JLabel();
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final Map<AbstractButton, T> buttonValueMap = new HashMap<>();

    /**
     * Constructs the radio group and initializes it with the specified enum choices.
     *
     * @param title   The title text for the group.
     * @param choices The Enum constants to create buttons for.
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
     * Creates JRadioButtons for the specified enum choices.
     * Requires each choice's enum type to have a public String text() method.
     *
     * @param choices The specific Enum constants to create buttons for.
     * @throws RuntimeException if the required text() method is missing or fails.
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
     * Creates the specific GomokuEvent to publish when selection changes.
     * Subclasses determine the exact event type.
     *
     * @param selectedValue The newly selected enum value.
     * @return The GomokuEvent for the selection change.
     */
    protected abstract GomokuEvent createSelectEvent(T selectedValue);

    /**
     * Adds an ActionListener to each button to publish selection events via the EventBus.
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


    private void subscribeToEvents() {
        subscribe(GameStartEvent.class, event -> setRadioButtonsEnabled(false));
        subscribe(GameOverEvent.class, event -> setRadioButtonsEnabled(true));
        subscribeToCustomEvents();
    }

    protected void subscribeToCustomEvents() {
        // Subclasses can override this method to subscribe to additional events
    }

    /**
     * Enables or disables all radio buttons and the title label in the group.
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
     * Programmatically selects the radio button corresponding to the given value.
     * Does nothing if the value is null or no button corresponds to the value.
     *
     * @param valueToSelect The Enum value to select.
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

}