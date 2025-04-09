package cn.wxiach;


import cn.wxiach.core.model.Board;
import cn.wxiach.core.model.Color;
import cn.wxiach.core.model.Stone;
import cn.wxiach.robot.search.AlphaBetaSearch;
import cn.wxiach.robot.search.TranspositionTable;
import cn.wxiach.robot.search.ZobristHash;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

public class AlphaBetaSearchTest {

    @Test
    void testAlphaBeta() {
        ZobristHash zobristHash = new ZobristHash();
        TranspositionTable transpositionTable = new TranspositionTable();
        AlphaBetaSearch alphaBetaSearch = new AlphaBetaSearch(zobristHash, transpositionTable);

        Board board = new Board();
        board.set(Board.index(7, 7), Color.BLACK.value());
        board.set(Board.index(7, 8), Color.WHITE.value());

        AtomicReference<Stone> stone = new AtomicReference<>();
        int value = alphaBetaSearch.execute(
                board,
                Color.BLACK,
                3,
                (result) -> stone.set((Stone) result)
        );
    }
}
