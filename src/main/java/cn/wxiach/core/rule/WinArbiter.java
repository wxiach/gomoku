package cn.wxiach.core.rule;


import cn.wxiach.model.Board;

public interface WinArbiter {
    boolean win(Board board);
}
