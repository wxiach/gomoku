package dev.gomoku.ai;

import dev.gomoku.model.Board;
import dev.gomoku.model.Move;
import dev.gomoku.model.Stone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 在独立的棋盘副本上执行迭代加深和 Negamax Alpha-Beta 搜索。
 * 每个节点的评分始终以该节点当前执子方为视角。
 * 同一实例一次只处理一次搜索；各轮迭代共享走法排序信息和置换表。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
public final class AlphaBetaEngine {
    private static final int INFINITY = PositionEvaluator.WIN_SCORE * 2;
    private static final int TRANSPOSITION_CAPACITY = 150_000;

    private static final int ASPIRATION_START_DEPTH = 3;
    // 期望窗口的单侧宽度，单位为棋型评分分值，不是搜索步数。
    private static final int ASPIRATION_MARGIN = 2_000;
    private static final int TIME_CHECK_INTERVAL = 64;

    private final PositionEvaluator evaluator = new PositionEvaluator();
    private final Map<Long, TableEntry> transpositions = new HashMap<>();

    private MoveGenerator moveGenerator;
    // 均采用单调时钟的纳秒值；deadline 是截止时刻，不是剩余时长。
    private long startedAt;
    private long deadline;

    private long nodes;
    private long cutoffs;
    private long tableHits;

    public SearchResult findBestMove(Board board, Stone player) {
        return findBestMove(board, player, SearchConfig.DEFAULT, null);
    }

    /**
     * 只通知已完整完成的迭代。回调在搜索线程执行，
     * 界面调用方需要自行将更新转交给事件派发线程。
     * 超时或中断时返回上一轮完整结果；尚无完整结果时返回合法的兜底走法。
     */
    public SearchResult findBestMove(Board source, Stone player, SearchConfig config,
                                     Consumer<SearchResult> onIterationComplete) {
        Objects.requireNonNull(source, "棋盘不能为空");
        Objects.requireNonNull(config, "搜索配置不能为空");

        if (player == null || player == Stone.EMPTY) {
            throw new IllegalArgumentException("搜索方必须是黑棋或白棋");
        }

        initializeSearch(config);

        // 搜索只修改副本，取消搜索不会影响正在对弈的棋盘。
        Board board = source.copy();
        if (board.isFull() || lastMoveWon(board)) {
            return result(null, 0, 0);
        }
        if (board.moveCount() == 0) {
            return result(new Move(Board.CENTER, Board.CENTER, player), 0, 0);
        }

        // 先准备合法走法，确保第一层尚未完成就超时时也有结果可返回。
        Move bestMove = legalFallback(board, player);
        int completedDepth = 0;
        int bestScore = 0;

        try {
            for (int depth = 1; depth <= config.maxDepth(); depth++) {
                checkTime();
                RootResult iteration = searchIteration(board, player, depth, bestScore, bestMove);

                // 整轮完成后再一起更新结果，避免中途取消时返回尚未搜索完整的答案。
                bestMove = iteration.move();
                bestScore = iteration.score();
                completedDepth = depth;

                if (onIterationComplete != null) {
                    onIterationComplete.accept(result(bestMove, completedDepth, bestScore));
                }

                // 终局评分包含步数修正；普通估值不会进入这个已证明胜负的分数区间。
                if (Math.abs(bestScore) >= PositionEvaluator.WIN_SCORE - SearchConfig.MAX_SUPPORTED_DEPTH) {
                    break;
                }
            }
        } catch (SearchStopped ignored) {
            // 保留上一轮完整迭代得到的走法。
        }

        return result(bestMove, completedDepth, bestScore);
    }

    private void initializeSearch(SearchConfig config) {
        startedAt = System.nanoTime();
        deadline = startedAt + config.timeLimit().toNanos();
        nodes = cutoffs = tableHits = 0;

        transpositions.clear();
        moveGenerator = new MoveGenerator(evaluator, config.candidateLimit());
    }

    /** 利用上一层的评分尝试窄窗口搜索，以减少需要访问的节点。 */
    private RootResult searchIteration(Board board, Stone player, int depth,
                                       int previousScore, Move previousBest) {
        if (depth < ASPIRATION_START_DEPTH) {
            return searchRoot(board, player, depth, -INFINITY, INFINITY, previousBest);
        }

        // alpha 是当前需要改善的下界；beta 是足以让上层放弃本分支的阈值。
        int alpha = Math.max(-INFINITY, previousScore - ASPIRATION_MARGIN);
        int beta = Math.min(INFINITY, previousScore + ASPIRATION_MARGIN);
        RootResult result = searchRoot(board, player, depth, alpha, beta, previousBest);

        // 评分超出期望窗口时，需要使用完整窗口重新搜索。
        if (result.score() <= alpha || result.score() >= beta) {
            return searchRoot(board, player, depth, -INFINITY, INFINITY, result.move());
        }

        return result;
    }

    /** 根节点需要保存最终走法；递归节点只需返回评分。 */
    private RootResult searchRoot(Board board, Stone player, int depth,
                                  int alpha, int beta, Move previousBest) {
        List<Move> moves = moveGenerator.generate(board, player, previousBest, 0, this::checkTime);
        Move bestMove = moves.getFirst();
        int bestScore = -INFINITY;
        // 排序后的第一步先建立评分基准，其余走法才使用 PVS 零窗口试探。
        boolean firstMove = true;

        for (Move move : moves) {
            checkTime();
            int score = searchMove(board, move, depth, alpha, beta, 0, firstMove);
            firstMove = false;

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }

            alpha = Math.max(alpha, score);
            if (alpha >= beta) {
                cutoffs++;
                break;
            }
        }

        return new RootResult(bestMove, bestScore);
    }

    private int searchMove(Board board, Move move, int depth, int alpha, int beta,
                           int ply, boolean firstMove) {
        board.place(move.row(), move.col(), move.stone());

        try {
            Stone opponent = move.stone().opposite();
            if (firstMove) {
                // Negamax 切换执子方后，评分取反，同时交换并取反窗口上下界。
                return -negamax(board, opponent, depth - 1, -beta, -alpha, ply + 1);
            }

            // PVS 先用零窗口试探后续走法，只有可能改善当前结果时才进行完整重搜。
            // 整数评分中，宽度为 1 的窗口只需判断它能否超过 alpha。
            int score = -negamax(board, opponent, depth - 1, -alpha - 1, -alpha, ply + 1);
            // 未超过 alpha 就无需重搜；达到 beta 已足够剪枝，也无需精确评分。
            if (score > alpha && score < beta) {
                score = -negamax(board, opponent, depth - 1, -beta, -alpha, ply + 1);
            }

            return score;
        } finally {
            // 即使深层搜索因超时退出，也必须撤销试走，恢复当前节点的局面。
            board.undo();
        }
    }

    /**
     * 在当前 [alpha, beta) 窗口内搜索，返回以当前执子方为视角的评分或评分边界。
     * depth 表示剩余搜索步数；ply 表示当前节点距离根节点的步数。
     *
     * @param alpha 搜索窗口下界，走法评分需超过它才可能改善当前结果
     * @param beta 搜索窗口上界，达到它即可停止遍历该节点的其余走法
     * @param ply 距离根节点的落子步数，用于终局分修正和杀手走法索引
     */
    private int negamax(Board board, Stone player, int depth, int alpha, int beta, int ply) {
        nodes++;
        if (nodes % TIME_CHECK_INTERVAL == 0) {
            checkTime();
        }

        // 上一步已经获胜，就意味着当前节点的执子方已经失败。
        // 步数修正使搜索倾向于更早获胜，并尽量推迟无法避免的失败。
        if (lastMoveWon(board)) {
            return -PositionEvaluator.WIN_SCORE + ply;
        }
        if (board.isFull()) {
            return 0;
        }

        if (depth == 0) {
            return evaluator.evaluate(board, player);
        }

        // 缓存可能收窄窗口；写回结果时仍需用入口边界判断精确值或上下界。
        int originalAlpha = alpha;
        int originalBeta = beta;
        long key = board.positionKey(player);
        TableEntry cached = transpositions.get(key);
        Move preferredMove = cached == null ? null : cached.bestMove();

        // 深度不足的缓存仍可用于走法排序，但不能直接代替当前深度的评分。
        if (cached != null && cached.depth() >= depth) {
            tableHits++;
            if (cached.bound() == Bound.EXACT) {
                return cached.score();
            }

            if (cached.bound() == Bound.LOWER) {
                // 下界只能抬高 alpha，上界只能降低 beta，不能直接当成精确值。
                alpha = Math.max(alpha, cached.score());
            } else {
                beta = Math.min(beta, cached.score());
            }

            if (alpha >= beta) {
                return cached.score();
            }
        }

        int bestScore = -INFINITY;
        Move bestMove = null;
        boolean firstMove = true;

        for (Move move : moveGenerator.generate(board, player, preferredMove, ply, this::checkTime)) {
            int score = searchMove(board, move, depth, alpha, beta, ply, firstMove);
            firstMove = false;

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }

            alpha = Math.max(alpha, score);
            if (alpha >= beta) {
                // 达到 beta 后，该分支已经足以让父节点放弃继续比较这里的其他走法。
                cutoffs++;
                moveGenerator.recordCutoff(player, move, depth, ply);
                break;
            }
        }

        storePosition(key, depth, bestScore, bestMove, originalAlpha, originalBeta);
        return bestScore;
    }

    /** 剪枝只能证明评分边界；落在原始窗口内部的结果才记为精确值。 */
    private void storePosition(long key, int depth, int score, Move bestMove, int alpha, int beta) {
        // 未超过原始 alpha 记为上界；达到 beta 记为下界；窗口内部才是精确值。
        Bound bound = score <= alpha ? Bound.UPPER : score >= beta ? Bound.LOWER : Bound.EXACT;
        TableEntry previous = transpositions.get(key);

        // 较浅的结果不覆盖较深的结果，保留更充分的搜索信息。
        if (previous != null && previous.depth() > depth) {
            return;
        }

        if (previous != null || transpositions.size() < TRANSPOSITION_CAPACITY) {
            transpositions.put(key, new TableEntry(depth, score, bound, bestMove));
        }
    }

    private boolean lastMoveWon(Board board) {
        Move lastMove = board.lastMove();
        return lastMove != null && board.winsAt(lastMove.row(), lastMove.col());
    }

    private Move legalFallback(Board board, Stone player) {
        Move nearest = null;
        int shortestDistance = Integer.MAX_VALUE;

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                int distance = Math.abs(row - Board.CENTER) + Math.abs(col - Board.CENTER);
                if (board.isEmpty(row, col) && distance < shortestDistance) {
                    shortestDistance = distance;
                    nearest = new Move(row, col, player);
                }
            }
        }

        return nearest;
    }

    private SearchResult result(Move move, int depth, int score) {
        long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
        return new SearchResult(move, depth, score, nodes, cutoffs, tableHits, elapsedMillis);
    }

    private void checkTime() {
        if (Thread.currentThread().isInterrupted() || System.nanoTime() >= deadline) {
            throw SearchStopped.INSTANCE;
        }
    }

    /**
     * 置换表中评分的证明类型：精确值、下界或上界。
     *
     * @author wxiach@gmail.com 2026/11/02
     */
    private enum Bound {
        // 当前选择性搜索及深度下的精确结果。
        EXACT,
        // 真正评分至少达到所记录的分值。
        LOWER,
        // 真正评分至多达到所记录的分值。
        UPPER
    }

    /**
     * 相同局面的缓存结果，用于复用评分边界和安排优先走法。
     *
     * @param depth 这条记录完成的剩余搜索深度
     * @param score 以该局面当前执子方为视角的评分或边界
     * @param bound 说明 score 是精确值、下界还是上界
     * @param bestMove 该局面中优先尝试的走法
     * @author wxiach@gmail.com 2026/11/02
     */
    private record TableEntry(int depth, int score, Bound bound, Move bestMove) {
    }

    /**
     * 一轮根节点搜索的走法与评分，期望窗口据此决定是否需要重新搜索。
     *
     * @author wxiach@gmail.com 2026/11/02
     */
    private record RootResult(Move move, int score) {
    }

    /**
     * 仅用于搜索内部的提前退出；复用无堆栈异常，减少取消搜索的额外开销。
     *
     * @author wxiach@gmail.com 2026/11/02
     */
    private static final class SearchStopped extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private static final SearchStopped INSTANCE = new SearchStopped();

        private SearchStopped() {
            super(null, null, false, false);
        }
    }
}
