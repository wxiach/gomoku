package cn.wxiach.model;


import cn.wxiach.config.GomokuConf;

public record Piece(Point point, Color color) {
    private static final Piece[][][] CACHE = new Piece[GomokuConf.BOARD_SIZE][GomokuConf.BOARD_SIZE][2];

    static {
        for (int x = 0; x < GomokuConf.BOARD_SIZE; x++) {
            for (int y = 0; y < 15; y++) {
                CACHE[x][y][0] = new Piece(Point.of(x, y), Color.BLACK);
                CACHE[x][y][1] = new Piece(Point.of(x, y), Color.WHITE);
            }
        }
    }

    public static Piece of(Point point, Color color) {
        return CACHE[point.x()][point.y()][color.getValue() - 1];
    }

    public static Piece of(int x, int y, Color color) {
        return of(Point.of(x, y), color);
    }
}
