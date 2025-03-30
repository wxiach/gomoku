package cn.wxiach.core.ai;

import cn.wxiach.core.ai.search.AlphaBetaSearch;
import cn.wxiach.core.state.support.BoardStateReadable;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.PiecePlaceEvent;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RobotEngine {

    private static final Logger logger = LoggerFactory.getLogger(RobotEngine.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void startCompute(BoardStateReadable board) {
        AlphaBetaSearch alphaBetaSearch = new AlphaBetaSearch(board.board(), board.opponentColor());
        executorService.submit(() -> {
            try {
                Piece piece;
                if (board.pieces().isEmpty()) {
                    piece = Piece.of(Board.BOARD_SIZE / 2, Board.BOARD_SIZE / 2, Color.BLACK);
                } else {
                    piece = alphaBetaSearch.execute();
                }
                if (piece == null) {
                    throw new RobotException("Robot compute a null point.");
                } else {
                    GomokuEventBus.getInstance().publish(new PiecePlaceEvent(this, piece));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
