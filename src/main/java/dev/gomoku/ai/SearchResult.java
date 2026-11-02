package dev.gomoku.ai;

import dev.gomoku.model.Move;

/**
 * 上一轮完整迭代的结果快照，附带本次思考累计的搜索统计。
 * score 以根节点执子方为视角；depth 为零表示开局、兜底或终局结果。
 * 传入棋盘已经终局时，move 为空。
 *
 * @param move 当前可以提交的走法，终局时为空
 * @param depth 最近完整结束的迭代深度，不包含因超时中断的深度
 * @param score 上述走法在已完成迭代中的评分或边界值
 * @param nodes 本次思考的递归访问次数，包含试探和重搜
 * @param cutoffs 遍历走法时触发的剪枝次数，不含置换表直接截断
 * @param tableHits 命中且深度足够、可以复用评分边界的置换表次数
 * @param elapsedMillis 从本次思考开始累计的毫秒数
 * @author wxiach@gmail.com 2026/11/02
 */
public record SearchResult(Move move, int depth, int score, long nodes,
                           long cutoffs, long tableHits, long elapsedMillis) {
}
