package cn.wxiach.core.state.rule;

public class OutOfBoardException extends RuntimeException {
    public OutOfBoardException(String message) {
        super(message);
    }
}
