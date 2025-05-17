package cn.wxiach.robot;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.types.StonePlaceEvent;
import cn.wxiach.gomoku.store.GomokuStore;
import cn.wxiach.gomoku.store.state.BoardState;
import cn.wxiach.gomoku.store.state.LevelState;
import cn.wxiach.gomoku.store.state.TurnState;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;
import cn.wxiach.pattern.Patterns;
import cn.wxiach.robot.search.AlphaBetaSearch;
import cn.wxiach.robot.support.EnhancedBoard;
import cn.wxiach.utils.Log;
import cn.wxiach.utils.MathUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RobotEngine {

		private final ExecutorService executorService = Executors.newSingleThreadExecutor();
		private final AlphaBetaSearch alphaBetaSearch = new AlphaBetaSearch();

		public void compute(GomokuStore store) {
				executorService.submit(() -> {
						try {
								Stone stone = doCompute(store.getBoardState(), store.getTurnState(), store.getLevelState());
								GomokuEventBus.getInstance().publish(new StonePlaceEvent(this, stone));
						} catch (Exception e) {
								Log.error("Robot compute error: ", e);
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

						AlphaBetaSearch.Result result = alphaBetaSearch.execute(enhancedBoard, turnState.currentTurn(), depth);

						if (MathUtils.approximateEqual(result.value(), Patterns.A5.value(), 1.15)) {
								Log.info("Iterative deepening Search finish in {} depth, the value is {}", depth, result.value());
								return result.stone();
						}

						stone = result.stone();
				}

				return stone;
		}
}
