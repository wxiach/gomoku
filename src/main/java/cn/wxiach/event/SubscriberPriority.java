package cn.wxiach.event;

/**
 * Defines the priority levels for event subscribers.
 * Higher integer values correspond to higher priority (executed first).
 */
public enum SubscriberPriority {
    /** For core game state updates, rule checks, and critical logic. */
    LOGIC(100),

    /** For UI updates, triggering subsequent actions (like AI compute). */
    UI(50), // Default priority

    /** For auxiliary effects like sound playback, logging. */
    EFFECT(0);

    private final int value;

    SubscriberPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
} 