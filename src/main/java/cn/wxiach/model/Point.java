package cn.wxiach.model;

import cn.wxiach.config.GomokuConf;


public record Point(int x, int y) {
    private static final Point[][] CACHE = new Point[GomokuConf.BOARD_SIZE][GomokuConf.BOARD_SIZE];

    static {
        for (int x = 0; x < GomokuConf.BOARD_SIZE; x++) {
            for (int y = 0; y < GomokuConf.BOARD_SIZE; y++) {
                CACHE[x][y] = new Point(x, y);
            }
        }
    }

    public static Point of(int x, int y) {
        return CACHE[x][y];
    }
}

