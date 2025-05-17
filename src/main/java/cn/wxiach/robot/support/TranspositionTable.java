package cn.wxiach.robot.support;

import cn.wxiach.model.Color;
import cn.wxiach.utils.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 置换表实现类，用于存储和检索棋局评估结果。
 * 采用单例模式设计，通过哈希值存储棋局位置的评估信息，以提高搜索算法效率。
 * <p>
 * 置换表可以避免重复搜索相同的棋局位置，通过记忆化技术减少计算量。
 * 存储的条目包含评估值、节点类型、搜索深度和棋子颜色等信息。
 *
 * @author wxiach 2025/5/10
 */
public enum TranspositionTable {
		INSTANCE;

		private final Map<Long, TranspositionTable.Entry> table = new ConcurrentHashMap<>();

		public static void store(long hash, int value, int type, int depth, Color color) {
				INSTANCE.storeEntry(hash, value, type, depth, color);
		}

		public static Integer find(long hash, int depth, int alpha, int beta, Color color) {
				return INSTANCE.findEntry(hash, depth, alpha, beta, color);
		}

		private void storeEntry(long hash, int value, int type, int depth, Color color) {
				TranspositionTable.Entry newEntry = new Entry(value, type, depth, color);

				table.compute(hash, (key, existingEntry) -> {
						if (existingEntry == null || newEntry.depth() >= existingEntry.depth()) {
								return newEntry;
						} else {
								return existingEntry;
						}
				});
		}

		/**
		 * 从置换表中查找指定哈希值对应的棋局评估信息。
		 * <p>
		 * 该方法通过哈希值查找缓存的评估结果，仅当缓存条目的搜索深度大于等于当前请求深度时才会使用。
		 * 这一深度限制非常必要，因为：
		 * 1. 同一个棋盘状态，在不同搜索深度下的评估值可能截然不同
		 * 2. 浅层搜索可能无法预见深层的关键战术变化
		 * 3. 在五子棋中，深度不足可能看不到"活三"变"冲四"等关键威胁
		 * 4. 使用浅层搜索结果可能导致错误的剪枝决策
		 * <p>
		 * 根据节点类型(EXACT、ALPHA、BETA)和当前alpha-beta值决定返回的评估值。
		 * 当缓存的颜色与当前搜索颜色不同时，会对评估值取反以匹配当前视角。
		 *
		 * @param hash  棋局的Zobrist哈希值
		 * @param depth 当前请求的搜索深度
		 * @param alpha alpha-beta剪枝的alpha值
		 * @param beta  alpha-beta剪枝的beta值
		 * @param color 当前行棋方的颜色
		 * @return 适用于当前搜索的评估值，若无匹配缓存或深度不足则返回null
		 */
		private Integer findEntry(long hash, int depth, int alpha, int beta, Color color) {
				Entry transpositionEntry = table.get(hash);
				if (transpositionEntry != null && transpositionEntry.depth() >= depth) {

						int value = transpositionEntry.value();
						if (transpositionEntry.color() != color) {
								value = -value;
						}

						Log.debug("Hit the cache in TranspositionTable. The value is: {}", value);

						if (transpositionEntry.type() == Entry.EXACT) {
								return value;
						}

						if (transpositionEntry.type() == Entry.BETA && value >= beta) {
								return beta;
						}

						if (transpositionEntry.type() == Entry.ALPHA && value <= alpha) {
								return alpha;
						}
				}
				return null;
		}

		public record Entry(int value, int type, int depth, Color color) {
				public static final int EXACT = 0;
				public static final int BETA = 1;
				public static final int ALPHA = 2;
		}
}
