package cn.wxiach.gomoku.rule;

public class PositionOccupiedException extends RuntimeException {

    public PositionOccupiedException(String message) {
        super(message);
    }
}
