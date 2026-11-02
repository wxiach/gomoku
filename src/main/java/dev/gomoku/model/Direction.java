package dev.gomoku.model;

import java.util.List;

/**
 * 四条胜负判定轴线对应的行、列单位步长；沿正反两个方向统计连线。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
public enum Direction {
    HORIZONTAL(0, 1),
    VERTICAL(1, 0),
    DIAGONAL(1, 1),
    ANTI_DIAGONAL(1, -1);

    public static final List<Direction> ALL = List.of(values());

    public final int rowStep;
    public final int colStep;

    Direction(int rowStep, int colStep) {
        this.rowStep = rowStep;
        this.colStep = colStep;
    }
}
