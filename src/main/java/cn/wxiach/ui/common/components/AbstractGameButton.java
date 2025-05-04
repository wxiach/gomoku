package cn.wxiach.ui.common.components;

import cn.wxiach.event.EventBusAware;
import cn.wxiach.event.SubscriberPriority;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract base class for all game control buttons.
 * Provides common functionality for game buttons including:
 * - Button text setup
 * - Event publishing (handled within handleButtonClick)
 * - Button state management
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
     * Subclasses must implement this method to define the action
     * to be performed when the button is clicked.
     * This might include publishing events.
     */
    protected abstract void handleButtonClick();

    /**
     * Subclasses must implement this method to subscribe to relevant events
     * that might affect the button's state (e.g., enable/disable).
     */
    protected abstract void subscribeToEvents();

    @Override
    public SubscriberPriority defaultSubscriberPriority() {
        return SubscriberPriority.UI;
    }
}