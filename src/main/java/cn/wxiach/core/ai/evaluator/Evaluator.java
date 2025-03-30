package cn.wxiach.core.ai.evaluator;


import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;

public interface Evaluator {

    /**
     * Score entire board
     *
     * @param board
     * @param color
     * @return
     */
    default int evaluate(char[][] board, Color color) {
        return evaluateForColor(board, color) - evaluateForColor(board, Color.reverse(color));
    }

    /**
     * Score all pieces of a certain color
     *
     * @param board
     * @param color
     * @return
     */
    int evaluateForColor(char[][] board, Color color);


    /**
     *
     * @param board
     * @param piece
     * @return
     */
    default int evaluatePieceOnLine(char[][] board, Piece piece) {
        return evaluatePieceOnLineForColor(board, piece)
                - evaluatePieceOnLineForColor(board, Piece.of(piece.point(), Color.reverse(piece.color())));
    }

    /**
     *
     * @param board
     * @param piece
     * @return
     */
    int evaluatePieceOnLineForColor(char[][] board, Piece piece);
}
