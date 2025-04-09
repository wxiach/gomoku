package cn.wxiach.core.model;

public enum Level {
    DIFFICULT(8), NORMAL(6), EASY(4);

    public final int value;

    Level(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
