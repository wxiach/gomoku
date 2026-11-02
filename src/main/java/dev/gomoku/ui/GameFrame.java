package dev.gomoku.ui;

import dev.gomoku.ai.AlphaBetaEngine;
import dev.gomoku.ai.SearchConfig;
import dev.gomoku.ai.SearchResult;
import dev.gomoku.model.Board;
import dev.gomoku.model.Game;
import dev.gomoku.model.Move;
import dev.gomoku.model.Stone;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * 协调 Swing 控件和游戏状态；对弈棋盘只在事件派发线程中修改。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
public final class GameFrame extends JFrame {
    private static final String WINDOW_TITLE = "五子棋";
    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(480, 540);
    private static final int CONTROL_GAP = 10;
    private static final int VERTICAL_PADDING = 4;
    private static final int HORIZONTAL_PADDING = 16;

    private final Game game = new Game();

    private final BoardPanel boardPanel = new BoardPanel(game.board(), this::humanMove);
    private final JComboBox<String> sideChoice = new JComboBox<>(new String[]{"执黑（先手）", "执白（后手）"});
    private final JButton undoButton = new JButton("悔棋");

    private Stone human = Stone.BLACK;
    // 唯一有权提交搜索结果的任务；为空表示当前没有有效的后台搜索。
    private AiWorker activeWorker;

    public GameFrame() {
        super(WINDOW_TITLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(MINIMUM_WINDOW_SIZE);

        add(boardPanel, BorderLayout.CENTER);
        add(createControls(), BorderLayout.SOUTH);

        sideChoice.addActionListener(event -> newGame());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                cancelSearch();
            }
        });

        pack();
        setLocationRelativeTo(null);
        newGame();
    }

    /** 在 Swing 事件派发线程中创建并显示窗口。 */
    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ignored) {
                // 系统外观不可用时，继续使用 Swing 默认外观。
            }

            new GameFrame().setVisible(true);
        });
    }

    private JPanel createControls() {
        JPanel controls = new JPanel(new BorderLayout(CONTROL_GAP, 0));
        JPanel sideControls = new JPanel(new FlowLayout(FlowLayout.LEFT, CONTROL_GAP, 0));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, CONTROL_GAP, 0));
        JButton newGameButton = new JButton("新局");
        controls.setBorder(BorderFactory.createEmptyBorder(
                VERTICAL_PADDING, HORIZONTAL_PADDING, VERTICAL_PADDING, HORIZONTAL_PADDING));

        sideControls.add(new JLabel("执子"));
        sideControls.add(sideChoice);
        actions.add(undoButton);
        actions.add(newGameButton);
        controls.add(sideControls, BorderLayout.WEST);
        controls.add(actions, BorderLayout.EAST);

        sideChoice.setToolTipText("切换执子将开始新局");
        newGameButton.addActionListener(event -> newGame());
        undoButton.addActionListener(event -> undoTurn());

        return controls;
    }

    private void newGame() {
        cancelSearch();
        human = sideChoice.getSelectedIndex() == 0 ? Stone.BLACK : Stone.WHITE;
        game.reset();
        setSearchMessage(" ");

        refresh();
        startAiTurn();
    }

    private void humanMove(int row, int col) {
        if (activeWorker == null && game.turn() == human && game.play(row, col)) {
            refresh();
            startAiTurn();
        }
    }

    private void undoTurn() {
        if (game.canUndo(human)) {
            // 先取消尚未完成的电脑应手，再回退对弈状态。
            cancelSearch();
            game.undoHumanTurn(human);
            setSearchMessage(" ");
            refresh();
        }
    }

    private void startAiTurn() {
        if (!game.isOver() && game.turn() != human && activeWorker == null) {
            // 在事件派发线程中复制局面，后台任务只接收独立的棋盘副本。
            activeWorker = new AiWorker(game.board().copy(), human.opposite());
            setSearchMessage("电脑思考中");
            refresh();
            activeWorker.execute();
        }
    }

    private void cancelSearch() {
        AiWorker previous = activeWorker;

        // 先解除当前任务的归属，让已排队的 process、done 回调无法再修改游戏。
        activeWorker = null;

        if (previous != null) {
            previous.cancel(true);
        }
    }

    private void refresh() {
        String status;

        if (game.isOver()) {
            status = game.winner() == Stone.EMPTY ? "和棋"
                    : game.winner() == human ? "你赢了" : "电脑获胜";
        } else {
            status = activeWorker != null ? "电脑思考中"
                    : game.turn() == human ? "轮到你落子" : "等待电脑落子";
        }

        boardPanel.setStatus("你执" + (human == Stone.BLACK ? "黑" : "白") + " · " + status);
        undoButton.setEnabled(game.canUndo(human));
        boardPanel.update(game.board(), !game.isOver() && activeWorker == null && game.turn() == human);
    }

    private void setSearchMessage(String message) {
        boardPanel.setSearchStats(message, null);
    }

    private void showStats(SearchResult result) {
        String summary = String.format("%,d 毫秒 · 深度 %d", result.elapsedMillis(), result.depth());
        String details = String.format("深度 %d | %,d 毫秒 | 节点 %,d | 剪枝 %,d | 缓存命中 %,d",
                result.depth(), result.elapsedMillis(), result.nodes(), result.cutoffs(), result.tableHits());
        boardPanel.setSearchStats(summary, details);
    }

    /**
     * doInBackground 在后台搜索；process 和 done 会回到事件派发线程执行。
     * 通过任务身份检查，丢弃悔棋、重开或关闭窗口之前已经排队的旧回调。
     *
     * @author wxiach@gmail.com 2026/11/02
     */
    private final class AiWorker extends SwingWorker<SearchResult, SearchResult> {
        private final Board position;
        private final Stone player;

        private AiWorker(Board position, Stone player) {
            this.position = position;
            this.player = player;
        }

        @Override
        protected SearchResult doInBackground() {
            return new AlphaBetaEngine().findBestMove(position, player, SearchConfig.DEFAULT, this::publish);
        }

        @Override
        protected void process(List<SearchResult> updates) {
            if (activeWorker == this && !updates.isEmpty()) {
                // 思考中只更新已完成的深度，不显示不断变化的耗时；结束后统一展示最终毫秒数。
                boardPanel.setSearchStats("深度 " + updates.getLast().depth(), null);
            }
        }

        @Override
        protected void done() {
            // 旧任务可能在重开后才结束，只有当前任务的结果允许落子。
            if (activeWorker != this) {
                return;
            }

            activeWorker = null;

            try {
                // done 在任务结束后调用，get 用于取结果或传播后台异常，不再等待搜索。
                SearchResult result = get();
                showStats(result);
                Move move = result.move();
                if (move == null || !game.play(move.row(), move.col())) {
                    throw new IllegalStateException("电脑搜索未返回合法走法");
                }
            } catch (CancellationException ignored) {
                // 悔棋或开始新局会正常取消任务，无需显示错误。
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException | IllegalStateException exception) {
                setSearchMessage("电脑搜索失败");
                JOptionPane.showMessageDialog(GameFrame.this,
                        "电脑搜索失败，可悔棋或重开。\n" + exception.getMessage(), WINDOW_TITLE, JOptionPane.ERROR_MESSAGE);
            }

            refresh();
        }
    }

}
