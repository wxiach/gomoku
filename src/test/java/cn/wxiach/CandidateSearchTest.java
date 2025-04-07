package cn.wxiach;

import cn.wxiach.ai.search.AlphaBetaSearch;
import cn.wxiach.ai.search.TranspositionTable;
import cn.wxiach.ai.search.ZobristHash;
import cn.wxiach.core.rule.BoardChecker;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CandidateSearchTest {

    private Board board;
    private AlphaBetaSearch.CandidateSearch candidateSearch;

    @BeforeEach
    public void setUp() {
        board = new Board();
        ZobristHash zobristHash = new ZobristHash();
        TranspositionTable transpositionTable = new TranspositionTable();
        AlphaBetaSearch alphaBetaSearch = new AlphaBetaSearch(zobristHash, transpositionTable);
        candidateSearch = alphaBetaSearch.new CandidateSearch();
    }

    @Test
    public void testObtainCandidatesCount() {
        Color color = Color.BLACK;

        // Simulating some moves on the board.
        board.set(Board.index(7, 7), Color.BLACK.value());
        board.set(Board.index(7, 8), Color.WHITE.value());
        board.set(Board.index(8, 7), Color.BLACK.value());

        // Obtain candidate moves for BLACK
        List<Stone> candidates = candidateSearch.obtainCandidates(board, color);

        assertNotNull(candidates);
        assertFalse(candidates.isEmpty());

        int candidateCount = candidates.size();
        assertEquals(10, candidateCount);

        Stone bestCandidate = candidates.getFirst();
        assertEquals(Color.BLACK, bestCandidate.color());
    }


    @Test
    public void testObtainCandidatesOrder() {
        Color color = Color.WHITE;

        // Simulating some moves on the board.
        board.set(Board.index(7, 7), Color.BLACK.value());
        board.set(Board.index(7, 6), Color.WHITE.value());
        board.set(Board.index(6, 6), Color.BLACK.value());
        board.set(Board.index(5, 5), Color.WHITE.value());
        board.set(Board.index(6, 7), Color.BLACK.value());
        board.set(Board.index(6, 5), Color.WHITE.value());
        board.set(Board.index(8, 7), Color.BLACK.value());
        board.set(Board.index(5, 7), Color.WHITE.value());
        board.set(Board.index(10, 7), Color.BLACK.value());


        // Obtain candidate moves for BLACK
        List<Stone> candidates = candidateSearch.obtainCandidates(board, color);

        assertNotNull(candidates);
        assertFalse(candidates.isEmpty());

        Stone bestCandidate = candidates.getFirst();
        assertEquals(Stone.of(9, 7, color), bestCandidate);
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testSearchSurroundBlankPoint() throws Exception {
        Method method = AlphaBetaSearch.CandidateSearch.class
                .getDeclaredMethod("searchSurroundBlankPoint", Board.class, Point.class, int.class);
        method.setAccessible(true);

        Point center = Point.of(7, 7);
        board.set(Board.index(center), Color.BLACK.value());
        Collection<Point> blankPoints = (Collection<Point>) method.invoke(candidateSearch, board, center, 2);

        assertNotNull(blankPoints);
        assertFalse(blankPoints.isEmpty());

        int blankPointCount = blankPoints.size();
        assertEquals(24, blankPointCount);

        blankPoints.forEach(point -> {
            assertTrue(BoardChecker.isEmpty(board, point));
        });
    }

}


