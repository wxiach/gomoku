package cn.wxiach.core.state;

import cn.wxiach.core.rule.BoardChecker;
import cn.wxiach.core.rule.PositionOccupiedException;
import cn.wxiach.model.Board;
import cn.wxiach.model.Stone;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;

public class BoardState extends TurnState implements BoardStateReadable {

    private final Board board = new Board();

    private final LinkedHashSet<Stone> stoneSequence = new LinkedHashSet<>();

    public Board board() {
        return this.board;
    }

    @Override
    public LinkedHashSet<Stone> stoneSequence() {
        return this.stoneSequence;
    }

    public void placeStone(Stone stone) {
        if (BoardChecker.isOccupied(board, stone.point())) {
            throw new PositionOccupiedException(String.format("(%s, %s) has a stone", stone.x(), stone.y()));
        }
        stoneSequence.addLast(stone);
        board.makeMove(stone);
    }

    public void revertStone(int count) {
        while (count > 0) {
            try {
                Stone stone = stoneSequence.removeLast();
                board.undoMove(stone);
            } catch (NoSuchElementException ignore) {
            } finally {
                count--;
            }
        }
    }

    @Override
    protected void reset() {
        super.reset();
        board.reset();
    }
}
