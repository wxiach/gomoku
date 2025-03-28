package cn.wxiach.model;

public enum Color {
    BLANK(0),
    BLACK(1),
    WHITE(2);

    private final int value;

    Color(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Color getColor(int value) {
        return switch (value) {
            case 0 -> BLANK;
            case 1 -> BLACK;
            case 2 -> WHITE;
            default -> throw new IllegalArgumentException("Unexpected value: " + value);
        };
    }
}

