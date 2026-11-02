package dev.gomoku.ai;

import java.time.Duration;
import java.util.Objects;

/**
 * 约束电脑单次思考的资源。深度按单方落子步数计算；candidateLimit 只限制
 * 普通候选走法，直接获胜和必须封堵的位置会全部保留。
 *
 * @param maxDepth 根节点最多向下搜索的单方落子步数
 * @param timeLimit 单次思考时限，由搜索主动检查，并非到点强制终止线程
 * @param candidateLimit 每个节点保留的普通候选数量上限
 * @author wxiach@gmail.com 2026/11/02
 */
public record SearchConfig(int maxDepth, Duration timeLimit, int candidateLimit) {
    public static final int MAX_SUPPORTED_DEPTH = 32;

    public static final SearchConfig DEFAULT = new SearchConfig(6, Duration.ofSeconds(2), 16);

    public SearchConfig {
        Objects.requireNonNull(timeLimit, "搜索时限不能为空");

        if (maxDepth < 1 || maxDepth > MAX_SUPPORTED_DEPTH
                || timeLimit.isNegative() || timeLimit.isZero() || candidateLimit < 1) {
            throw new IllegalArgumentException("搜索深度必须在允许范围内，搜索时限和候选数量必须大于零");
        }
    }
}
