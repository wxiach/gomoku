package dev.gomoku.model;

/**
 * 管理一局本地人机对弈的回合推进、胜负和悔棋。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
public final class Game {
    private Board board = new Board();
    private Stone turn = Stone.BLACK;
    // EMPTY 表示尚无胜方；与棋盘是否已满结合后才能区分进行中和和棋。
    private Stone winner = Stone.EMPTY;

    /** 无效落子或终局后的点击不会修改棋盘，也不会消耗当前回合。 */
    public boolean play(int row, int col) {
        if (isOver() || !board.isEmpty(row, col)) {
            return false;
        }

        board.place(row, col, turn);
        if (board.winsAt(row, col)) {
            winner = turn;
        }

        turn = turn.opposite();
        return true;
    }

    public void reset() {
        board = new Board();
        turn = Stone.BLACK;
        winner = Stone.EMPTY;
    }

    public boolean canUndo(Stone human) {
        return board.moves().stream().anyMatch(move -> move.stone() == human);
    }

    /**
     * 撤销到玩家上一次落子之前，同时移除这一步之后电脑的应手。
     * 电脑仍在思考时尚未产生应手，因此只需撤销玩家刚下的一步。
     * 玩家执白时，悔棋会保留电脑的黑棋开局。
     */
    public boolean undoHumanTurn(Stone human) {
        if (!canUndo(human)) {
            return false;
        }

        Move removed;
        // 从尾部撤销，直到玩家自己的上一步也被移除；更早的电脑开局不会被触及。
        do {
            removed = board.undo();
        } while (removed.stone() != human);

        // 原有胜负由已撤销的局面产生，回退后应重新开放玩家回合。
        winner = Stone.EMPTY;
        turn = human;
        return true;
    }

    public Board board() {
        return board;
    }

    public Stone turn() {
        return turn;
    }

    public Stone winner() {
        return winner;
    }

    public boolean isOver() {
        return winner != Stone.EMPTY || board.isFull();
    }
}
