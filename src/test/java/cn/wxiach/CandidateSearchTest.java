package cn.wxiach;

import cn.wxiach.gomoku.rule.BoardCheck;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.robot.search.CandidateSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class CandidateSearchTest {

    private final CandidateSearch candidateSearch = new CandidateSearch();
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testSearchSurroundBlankPoints() {

        Point center = Point.of(7, 7);
        board.set(Board.index(center), Color.BLACK.value());
        Collection<Point> blankPoints = candidateSearch.searchSurroundBlankPoints(board, center, 2);

        assertNotNull(blankPoints);
        assertFalse(blankPoints.isEmpty());

        int blankPointCount = blankPoints.size();
        assertEquals(24, blankPointCount);

        blankPoints.forEach(point -> {
            assertTrue(BoardCheck.isEmpty(board, point));
        });
    }

}


