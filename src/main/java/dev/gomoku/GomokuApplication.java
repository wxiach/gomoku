package dev.gomoku;

import dev.gomoku.ui.GameFrame;

import java.awt.GraphicsEnvironment;

/**
 * 桌面程序入口，Swing 窗口的初始化交给事件派发线程执行。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
public final class GomokuApplication {

    private GomokuApplication() {
    }

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("五子棋需要图形桌面环境。启动命令：java -jar target/gomoku-0.1.0-SNAPSHOT.jar");
            return;
        }

        GameFrame.launch();
    }
}
