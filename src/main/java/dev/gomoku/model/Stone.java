package dev.gomoku.model;

import java.util.List;

/**
 * 棋盘格的状态，区分空位、黑棋和白棋。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
public enum Stone {
    EMPTY(-1), BLACK(0), WHITE(1);

    public static final List<Stone> PLAYERS = List.of(BLACK, WHITE);

    private final int playerIndex;

    Stone(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    /** 返回玩家在哈希表和历史分数组中的紧凑下标；空位不对应任何玩家。 */
    public int playerIndex() {
        if (this == EMPTY) {
            throw new IllegalStateException("空位不属于任何执子方");
        }

        return playerIndex;
    }

    public Stone opposite() {
        return switch (this) {
            case BLACK -> WHITE;
            case WHITE -> BLACK;
            case EMPTY -> throw new IllegalStateException("空位不属于任何执子方");
        };
    }
}
