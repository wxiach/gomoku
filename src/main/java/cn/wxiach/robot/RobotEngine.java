package cn.wxiach.robot;

import cn.wxiach.core.store.GomokuStore;
import cn.wxiach.core.store.state.BoardState;
import cn.wxiach.core.store.state.LevelState;
import cn.wxiach.core.store.state.TurnState;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.types.StonePlaceEvent;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;
import cn.wxiach.pattern.Patterns;
import cn.wxiach.robot.search.CandidateSearch;
import cn.wxiach.robot.support.EnhancedBoard;
import cn.wxiach.utils.Log;
import cn.wxiach.utils.MathUtils;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RobotEngine {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final CandidateSearch candidateSearch = new CandidateSearch();

    public void compute(GomokuStore store) {
        executorService.submit(() -> {
            try {
                Stone stone = doCompute(store.getBoardState(), store.getTurnState(), store.getLevelState());
                GomokuEventBus.getInstance().publish(new StonePlaceEvent(this, stone));
            } catch (Exception e) {
                throw new RobotException("Robot compute error", e);
            }
        });
    }

    private Stone doCompute(BoardState boardState, TurnState turnState, LevelState levelState) {
        // 如果棋盘为空，则表示是电脑执黑先行，则默认返回棋盘中心点作为落子点
        if (boardState.stoneSequence().isEmpty()) {
            return Stone.of(Point.of(7, 7), Color.BLACK);
        }

        // 记录计算出的最佳落子点
        Stone stone = null;

        // 获取AI最大搜索深度
        int maxDepth = levelState.getLevel().value();

        // 使用迭代加深搜索配合Alpha-Beta搜索找到最优解
        for (int depth = 2; depth <= maxDepth; depth += 2) {
            Log.debug("Iterative deepening search start in {} depth.", depth);

            // 创建Board的包装类EnhancedBoard, 主要是为了可以实现对棋盘的增量式评分
            EnhancedBoard enhancedBoard = new EnhancedBoard(boardState.board());

            Optional<CandidateSearch.Result> result = candidateSearch.execute(enhancedBoard, turnState.currentTurn(), depth);

            if (result.isPresent()) {
                // 记录当前计算出的最佳落子点
                stone = result.get().stone();

                // 如果当前计算出的最佳落子点的评估值等于A5，则表示当前局面是必赢局面
                int resultValue = result.get().value();
                if (MathUtils.approximateEqual(resultValue, Patterns.A5.value(), 1.15)) {
                    Log.info("Iterative deepening Search finish in {} depth, the value is {}", depth, resultValue);
                    // 如果当前局面是必赢局面，则直接返回当前计算出的最佳落子点
                    return stone;
                }
            }
        }

        return stone;
    }
}
