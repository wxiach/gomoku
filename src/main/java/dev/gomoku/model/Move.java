package dev.gomoku.model;

/**
 * 一次落子记录，行列坐标均从零开始。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
public record Move(int row, int col, Stone stone) {
}
