package cn.wxiach.core.model;

public class Piece {
    private final int x;
    private final int y;
    private final Color color;

    public Piece(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    Color getColor() {
        return color;
    }

    public enum Color {
        BLACK(1),
        WHITE(2);

        private final int value;

        Color(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

}
