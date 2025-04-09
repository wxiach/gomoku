package cn.wxiach.core.model;

public enum Color {
    EMPTY('0'),
    BLACK('1'),
    WHITE('2');

    private final char value;

    Color(char value) {
        this.value = value;
    }

    public char value() {
        return value;
    }

    public String toString() {
        if (value == BLACK.value) return "black";
        else if (value == WHITE.value) return "white";
        return "empty";
    }

    public static Color of(char value) {
        return switch (value) {
            case '0' -> EMPTY;
            case '1' -> BLACK;
            case '2' -> WHITE;
            default -> throw new IllegalArgumentException("Unexpected value: " + value);
        };
    }

    /**
     * Used to switch stone's color between black and white.
     */
    public static Color reverse(Color color) {
        if (color == Color.EMPTY) return color;
        return color == Color.BLACK ? Color.WHITE : Color.BLACK;
    }
}

