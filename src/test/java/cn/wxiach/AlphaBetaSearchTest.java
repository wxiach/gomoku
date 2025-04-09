package cn.wxiach;


import cn.wxiach.ai.search.AlphaBetaSearch;
import cn.wxiach.ai.search.TranspositionTable;
import cn.wxiach.ai.search.ZobristHash;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Stone;
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
