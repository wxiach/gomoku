package cn.wxiach.model;

public enum Color {
    EMPTY('0', ""),
    BLACK('1', "黑棋（先手）"),
    WHITE('2', "白棋（后手）");

    private final char value;
    private final String text;

    Color(char value, String text) {
        this.value = value;
        this.text = text;
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
     * 用于在黑白棋之间切换棋子的颜色。
     */
    public static Color reverse(Color color) {
        if (color == Color.EMPTY) return color;
        return color == Color.BLACK ? Color.WHITE : Color.BLACK;
    }

    public char value() {
        return value;
    }

    public String text() {
        return text;
    }
}

