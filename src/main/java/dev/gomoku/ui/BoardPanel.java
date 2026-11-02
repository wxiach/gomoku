package dev.gomoku.ui;

import dev.gomoku.model.Board;
import dev.gomoku.model.Move;
import dev.gomoku.model.Stone;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 绘制棋盘并转换点击坐标；由 GameFrame 结合对弈状态决定是否接受落子。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
public final class BoardPanel extends JPanel {
    private static final int PREFERRED_SIZE = 640;
    private static final int HORIZONTAL_PADDING = 25;
    private static final int FIRST_ROW_TOP_GAP = 10;
    private static final int BOTTOM_PADDING = 40;
    private static final int LABEL_GAP = 10;
    private static final int INFORMATION_TOP_PADDING = 5;
    private static final int INFORMATION_GAP = 12;
    private static final int STAR_RADIUS = 3;
    private static final int LAST_MOVE_RING_GAP = 2;
    private static final float LAST_MOVE_RING_WIDTH = 2;
    private static final float WIN_LINE_WIDTH = 3;

    private static final double STONE_SIZE_RATIO = .8;
    // 点击与交叉点的横纵距离容差，按格距的比例计算，缩放后判定范围同步变化。
    private static final double CLICK_TOLERANCE = .46;
    private static final double NUMBER_SIZE_RATIO = .3;

    private static final Color BOARD_COLOR = new Color(235, 205, 157);
    // 标准十五路棋盘的五个星位，数组中的坐标依次为行、列。
    private static final int[][] STAR_POINTS = {{3, 3}, {3, 11}, {7, 7}, {11, 3}, {11, 11}};

    private final JLabel statusLabel = new JLabel();
    private final JLabel searchLabel = new JLabel("", SwingConstants.RIGHT);
    private final JPanel informationPanel = new JPanel(new BorderLayout(INFORMATION_GAP, 0));

    private Board board;
    private boolean interactive;
    private boolean showNumbers = true;
    private boolean showCoordinates = true;

    public BoardPanel(Board board, BiConsumer<Integer, Integer> onMove) {
        this.board = board;
        setLayout(new BorderLayout());
        setBackground(BOARD_COLOR);
        setPreferredSize(new Dimension(PREFERRED_SIZE, PREFERRED_SIZE));
        getAccessibleContext().setAccessibleName("五子棋棋盘，十五行十五列");

        informationPanel.setOpaque(false);
        informationPanel.setBorder(BorderFactory.createEmptyBorder(
                INFORMATION_TOP_PADDING, HORIZONTAL_PADDING, 0, HORIZONTAL_PADDING));
        informationPanel.add(statusLabel, BorderLayout.WEST);
        informationPanel.add(searchLabel, BorderLayout.CENTER);
        informationPanel.setVisible(false);
        add(informationPanel, BorderLayout.NORTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (!interactive || !SwingUtilities.isLeftMouseButton(event)) {
                    return;
                }

                // 绘制与点击判定必须使用同一套随窗口尺寸变化的居中网格。
                BoardGeometry geometry = geometry();
                // 先扣除网格左上偏移，再除以格距并四舍五入，找到最近的交叉点。
                int col = (int) Math.round((event.getX() - geometry.left()) / geometry.spacing());
                int row = (int) Math.round((event.getY() - geometry.top()) / geometry.spacing());
                double tolerance = geometry.spacing() * CLICK_TOLERANCE;

                // 最近交叉点仍可能离点击很远；必须检查棋盘范围、空位和距离容差。
                if (board.isEmpty(row, col) && Math.abs(event.getX() - geometry.x(col)) <= tolerance
                        && Math.abs(event.getY() - geometry.y(row)) <= tolerance) {
                    onMove.accept(row, col);
                }
            }
        });
    }

    public void update(Board board, boolean interactive) {
        this.board = board;
        this.interactive = interactive;
        setCursor(Cursor.getPredefinedCursor(interactive ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        repaint();
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
        informationPanel.setVisible(true);
        revalidate();
        repaint();
    }

    public void setSearchStats(String summary, String details) {
        searchLabel.setText(summary);
        searchLabel.setToolTipText(details);
    }

    public void setShowNumbers(boolean showNumbers) {
        this.showNumbers = showNumbers;
        repaint();
    }

    public void setShowCoordinates(boolean showCoordinates) {
        this.showCoordinates = showCoordinates;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        updateInformationInsets();
        Graphics2D canvas = (Graphics2D) graphics.create();

        try {
            canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            BoardGeometry geometry = geometry();

            drawGrid(canvas, geometry);
            drawWinningLine(canvas, geometry);
            // 连线从棋子下方经过，避免遮挡落子序号。
            drawStones(canvas, geometry);
            drawLastMoveMarker(canvas, geometry);
        } finally {
            // 仅释放本次创建的画布副本，不改变 Swing 传入画布的颜色、字体和线宽。
            canvas.dispose();
        }
    }

    private void drawGrid(Graphics2D canvas, BoardGeometry geometry) {
        canvas.setColor(Color.DARK_GRAY);
        canvas.setStroke(new BasicStroke());

        for (int index = 0; index < Board.SIZE; index++) {
            int x = geometry.x(index);
            int y = geometry.y(index);

            canvas.drawLine(geometry.left(), y, geometry.x(Board.SIZE - 1), y);
            canvas.drawLine(x, geometry.top(), x, geometry.y(Board.SIZE - 1));

            if (showCoordinates) {
                String rowLabel = Integer.toString(Board.SIZE - index);
                canvas.drawString(String.valueOf((char) ('A' + index)), x - canvas.getFontMetrics().charWidth('A') / 2,
                        geometry.y(Board.SIZE - 1) + canvas.getFontMetrics().getAscent() + LABEL_GAP);
                canvas.drawString(rowLabel, geometry.left() - canvas.getFontMetrics().stringWidth(rowLabel) - LABEL_GAP,
                        y + canvas.getFontMetrics().getAscent() / 2);
            }
        }

        for (int[] point : STAR_POINTS) {
            canvas.fillOval(geometry.x(point[1]) - STAR_RADIUS, geometry.y(point[0]) - STAR_RADIUS,
                    STAR_RADIUS * 2, STAR_RADIUS * 2);
        }
    }

    private void drawStones(Graphics2D canvas, BoardGeometry geometry) {
        List<Move> moves = board.moves();
        int diameter = (int) (geometry.spacing() * STONE_SIZE_RATIO);
        // 获胜连线使用粗画笔，棋子边缘始终恢复为普通细线。
        canvas.setStroke(new BasicStroke());
        canvas.setFont(getFont().deriveFont((float) (geometry.spacing() * NUMBER_SIZE_RATIO)));

        for (int index = 0; index < moves.size(); index++) {
            Move move = moves.get(index);
            int x = geometry.x(move.col());
            int y = geometry.y(move.row());

            canvas.setColor(move.stone() == Stone.BLACK ? Color.BLACK : Color.WHITE);
            canvas.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
            canvas.setColor(Color.DARK_GRAY);
            canvas.drawOval(x - diameter / 2, y - diameter / 2, diameter, diameter);

            if (showNumbers) {
                String number = Integer.toString(index + 1);
                canvas.setColor(move.stone() == Stone.BLACK ? Color.WHITE : Color.BLACK);
                // drawString 使用文字基线，结合上伸和下伸高度才能让数字在棋子内居中。
                int baseline = y + (canvas.getFontMetrics().getAscent() - canvas.getFontMetrics().getDescent()) / 2;
                canvas.drawString(number, x - canvas.getFontMetrics().stringWidth(number) / 2, baseline);
            }
        }
    }

    private void drawWinningLine(Graphics2D canvas, BoardGeometry geometry) {
        List<Move> line = board.winningLine();
        if (!line.isEmpty()) {
            Move first = line.getFirst();
            Move last = line.getLast();
            canvas.setColor(Color.RED);
            canvas.setStroke(new BasicStroke(WIN_LINE_WIDTH));
            canvas.drawLine(geometry.x(first.col()), geometry.y(first.row()),
                    geometry.x(last.col()), geometry.y(last.row()));
        }
    }

    private void drawLastMoveMarker(Graphics2D canvas, BoardGeometry geometry) {
        Move last = board.lastMove();
        if (last != null) {
            int diameter = (int) (geometry.spacing() * STONE_SIZE_RATIO) + LAST_MOVE_RING_GAP * 2;
            canvas.setColor(Color.RED);
            canvas.setStroke(new BasicStroke(LAST_MOVE_RING_WIDTH));
            canvas.drawOval(geometry.x(last.col()) - diameter / 2,
                    geometry.y(last.row()) - diameter / 2, diameter, diameter);
        }
    }

    private BoardGeometry geometry() {
        // 按信息文字的实际高度预留顶部空间，第一行棋子和坐标不会与角落信息重叠。
        int informationHeight = informationPanel.isVisible() ? informationPanel.getPreferredSize().height : 0;
        int topPadding = informationHeight + FIRST_ROW_TOP_GAP;
        int availableHeight = Math.max(1, getHeight() - topPadding - BOTTOM_PADDING);
        // 水平方向与格子间距保持一致，避免窗口缩放时棋子被压扁；底部独立留宽显示字母。
        int span = Math.max(1, Math.min(getWidth() - HORIZONTAL_PADDING * 2, availableHeight));
        return new BoardGeometry((getWidth() - span) / 2, topPadding, span / (Board.SIZE - 1.0));
    }

    private void updateInformationInsets() {
        // 两侧文字与最外侧棋线对齐，窗口缩放时同步跟随。
        int inset = geometry().left();
        informationPanel.setBorder(BorderFactory.createEmptyBorder(
                INFORMATION_TOP_PADDING, inset, 0, inset));
    }

    /**
     * 绘制和点击共用的行列到像素坐标映射，根据面板当前尺寸重新计算。
     *
     * @param left 第一条竖线相对面板左侧的像素偏移
     * @param top 第一条横线相对面板顶部的像素偏移
     * @param spacing 相邻交叉点的像素间距，保留小数以降低缩放后的累计误差
     * @author wxiach@gmail.com 2026/11/02
     */
    private record BoardGeometry(int left, int top, double spacing) {
        int x(int col) {
            return (int) Math.round(left + col * spacing);
        }

        int y(int row) {
            return (int) Math.round(top + row * spacing);
        }
    }
}
