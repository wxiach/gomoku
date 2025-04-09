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
        candidateSearch = new AlphaBetaSearch.CandidateSearch();
    }


    /**
     * Testing the priority of different patterns in the candidate list.
     * <p>
     * o o o o
     * + + +
     */
    @Test
    public void testObtainCandidatesOrder1() {
        Color color = Color.WHITE;


        board.set(Board.index(7, 7), Color.BLACK.value());
        board.set(Board.index(8, 7), Color.BLACK.value());
        board.set(Board.index(9, 7), Color.BLACK.value());
        board.set(Board.index(10, 7), Color.BLACK.value());

        board.set(Board.index(7, 8), Color.WHITE.value());
        board.set(Board.index(8, 8), Color.WHITE.value());
        board.set(Board.index(9, 8), Color.WHITE.value());

        // Obtain candidate moves for BLACK
        List<Stone> candidates = candidateSearch.obtainCandidates(board, color);

        assertNotNull(candidates);
        assertFalse(candidates.isEmpty());

        Stone bestCandidate = candidates.getFirst();
        assertEquals(Stone.of(6, 7, color), bestCandidate);
    }


    /**
     * o o o _ o
     * + + +
     */
    @Test
    public void testObtainCandidatesOrder2() {
        Color color = Color.WHITE;

        board.set(Board.index(7, 7), Color.BLACK.value());
        board.set(Board.index(8, 7), Color.BLACK.value());
        board.set(Board.index(9, 7), Color.BLACK.value());
        board.set(Board.index(11, 7), Color.BLACK.value());

        board.set(Board.index(7, 8), Color.WHITE.value());
        board.set(Board.index(8, 8), Color.WHITE.value());
        board.set(Board.index(9, 8), Color.WHITE.value());

        // Obtain candidate moves for BLACK
        List<Stone> candidates = candidateSearch.obtainCandidates(board, color);

        assertNotNull(candidates);
        assertFalse(candidates.isEmpty());

        Stone bestCandidate = candidates.getFirst();
        assertEquals(Stone.of(10, 7, color), bestCandidate);
    }


    /*
     * o
     * o o
     * + +
     */
    @Test
    public void testObtainCandidatesOrder3() {
        Color color = Color.WHITE;


        board.set(Board.index(7, 7), Color.BLACK.value());
        board.set(Board.index(7, 8), Color.BLACK.value());
        board.set(Board.index(8, 8), Color.BLACK.value());

        board.set(Board.index(7, 9), Color.WHITE.value());
        board.set(Board.index(8, 9), Color.WHITE.value());

        // Obtain candidate moves for BLACK
        List<Stone> candidates = candidateSearch.obtainCandidates(board, color);

        assertNotNull(candidates);
        assertFalse(candidates.isEmpty());

        Stone bestCandidate = candidates.getFirst();
        assertEquals(Stone.of(9, 9, color), bestCandidate);
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


