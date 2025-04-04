package cn.wxiach.ai;

import cn.wxiach.ai.evaluate.GomokuEvaluator;
import cn.wxiach.ai.search.AlphaBetaSearch;
import cn.wxiach.ai.search.KillSearch;
import cn.wxiach.ai.search.SearchContext;
import cn.wxiach.ai.search.SearchResult;
import cn.wxiach.ai.support.IterativeDeepeningSearch;
import cn.wxiach.ai.support.TranspositionTable;
import cn.wxiach.ai.support.ZobristHash;
import cn.wxiach.core.state.support.GameStateReadable;
import cn.wxiach.model.Piece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BestMoveGenerator {

    private final static Logger logger = LoggerFactory.getLogger(BestMoveGenerator.class);

    private final GomokuEvaluator evaluator = new GomokuEvaluator();

    private final ZobristHash zobristHash = new ZobristHash();
    private final TranspositionTable transpositionTable = new TranspositionTable();

    private final IterativeDeepeningSearch iterativeDeepeningSearch;

    private final KillSearch killSearch = new KillSearch(evaluator);
    private final AlphaBetaSearch alphaBetaSearch = new AlphaBetaSearch(zobristHash, transpositionTable, evaluator);

    private int maxSearchDepth;

    private SearchResult searchResult;

    public BestMoveGenerator(int maxSearchDepth) {
        this.maxSearchDepth = maxSearchDepth;
        iterativeDeepeningSearch = new IterativeDeepeningSearch(this.maxSearchDepth);
    }

    public Piece execute(GameStateReadable state) {

//        searchResult = killSearch.execute(new SearchContext(state.copyBoard(), state.currentTurn(), 6));
//        if (searchResult != null) {
//            return searchResult.piece();
//        }

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
