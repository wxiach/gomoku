package cn.wxiach.model;

public enum Color {
    EMPTY('0'),
    BLACK('1'),
    WHITE('2');

    private final char value;

    Color(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public String toString() {
        if (value == BLACK.value) return "black";
        else if (value == WHITE.value) return "white";
        return "empty";
    }

    public static Color getColor(char value) {
        return switch (value) {
            case '0' -> EMPTY;
            case '1' -> BLACK;
            case '2' -> WHITE;
            default -> throw new IllegalArgumentException("Unexpected value: " + value);
        };
    }

    // Used to switch between black and white
    public static Color reverse(Color color) {
        return color == Color.BLACK ? Color.WHITE : Color.BLACK;
    }
}

