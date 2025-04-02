package cn.wxiach.ai;

import cn.wxiach.ai.search.AlphaBetaSearch;
import cn.wxiach.ai.search.IterativeDeepeningSearch;
import cn.wxiach.ai.search.SearchContext;
import cn.wxiach.ai.support.TranspositionTable;
import cn.wxiach.ai.support.ZobristHash;
import cn.wxiach.core.state.support.GameStateReadable;
import cn.wxiach.model.Piece;

public class BestMoveGenerate {

    private final ZobristHash zobristHash = new ZobristHash();
    private final TranspositionTable transpositionTable = new TranspositionTable();

    private final AlphaBetaSearch alphaBetaSearch = new AlphaBetaSearch(zobristHash, transpositionTable);
    private final IterativeDeepeningSearch iterativeDeepeningSearch;
    private int maxSearchDepth;

    private AlphaBetaSearch.Result searchResult;

    public BestMoveGenerate(int maxSearchDepth) {
        this.maxSearchDepth = maxSearchDepth;
        iterativeDeepeningSearch = new IterativeDeepeningSearch(this.maxSearchDepth);
    }

    public Piece execute(GameStateReadable state) {
        this.iterativeDeepeningSearch.search(
                (depth) -> {
                    SearchContext searchContext = new SearchContext(state.copyBoard(), state.currentTurn(), depth);
                    searchResult = alphaBetaSearch.execute(searchContext);
                    return searchResult;
                }
        );
        return searchResult.piece();
    }

    public void setSearchDepth(int maxSearchDepth) {
        this.maxSearchDepth = maxSearchDepth;
    }
}
