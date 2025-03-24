package cn.wxiach;

import cn.wxiach.ui.GomokuWindow;


public class Gomoku {

    public static void main(String[] args) {
        new Gomoku().run();
    }

    public void run() {
        new GomokuWindow().getGameFlow().startGame();
    }
}