package cn.wxiach.event;

public abstract class GomokuEvent {
    private final Object source;

    public GomokuEvent(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}
