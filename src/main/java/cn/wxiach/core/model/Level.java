package cn.wxiach.core.model;

public enum Level {
    HARD(8, "困难（8层）"), MEDIUM(6, "普通（6层）"), EASY(4, "简单（4层）");

    private final int value;
    private final String text;

    Level(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int value() {
        return value;
    }

    public String text() {
        return text;
    }
}
