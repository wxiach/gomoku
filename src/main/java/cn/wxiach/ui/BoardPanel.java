package cn.wxiach.ui;

import cn.wxiach.core.model.Board;
import cn.wxiach.core.model.Color;
import cn.wxiach.core.model.Point;
import cn.wxiach.core.model.Stone;
import cn.wxiach.core.rule.BoardChecker;
import cn.wxiach.core.state.GameStateReadable;
import cn.wxiach.core.utils.SetUtils;
import cn.wxiach.event.EventBusAware;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.BoardUpdateEvent;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.event.support.StonePlaceEvent;
import cn.wxiach.ui.assets.FontAssets;
import cn.wxiach.ui.assets.ImageAssets;
import cn.wxiach.ui.support.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

public class BoardPanel extends JPanel implements EventBusAware {

    public static final int UNIT_DIMENSION = 40;

    /**
     * OFFSET equals UNIT_SIZE which is both easy to calculate and nice to look at
     */
    public static final int OFFSET = UNIT_DIMENSION;
    public static final int STONE_DIMENSION = 30;

    private static final int BOARD_DIMENSION = (Board.SIZE - 1) * UNIT_DIMENSION;

    private GameStateReadable state;

    private Point cursorTip;

    public BoardPanel() {
        setPreferredSize(new Dimension(BOARD_DIMENSION + (2 * OFFSET), BOARD_DIMENSION + (2 * OFFSET)));
        setEnabled(false);
        addMouseListeners();
        subscribeToEvents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Calling the super method ensures that other components are painted correctly.
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 1. Turn on all optimizations at once
        enableGraphicsOptimizations(g2d);

        g2d.drawImage(ImageAssets.getBoardImage(), 0, 0, getWidth(), getHeight(), this);

        // 2. Draw board coordinate
        g2d.setFont(FontAssets.LXGWWenKaiMonoScreen.deriveFont(Font.BOLD, 14f));
        g2d.setColor(java.awt.Color.BLACK);

        char[] verticalCoordinateTable = "ABCDEFGHJKLMNOP".toCharArray();

        for (int i = 0; i < Board.SIZE; i++) {
            // Horizontal coordinate
            String horizontalCoordinateValue = String.valueOf(i + 1);
            int textWidth = g2d.getFontMetrics().stringWidth(horizontalCoordinateValue);
            g2d.drawString(horizontalCoordinateValue, UNIT_DIMENSION - textWidth - 16, UNIT_DIMENSION + i * UNIT_DIMENSION + 4);
            // Vertical coordinate
            String verticalCoordinateValue = String.valueOf(verticalCoordinateTable[i]);
            g2d.drawString(verticalCoordinateValue, UNIT_DIMENSION + i * UNIT_DIMENSION - 5, 24);
        }

        // Steps 3 to 6 will have been completed on the board,
        // so move the draw origin to the top-left boundary of the board.
        g2d.translate(OFFSET, OFFSET);

        // 3. Draw board lines with a margin around the edges.
        g2d.setStroke(new BasicStroke(1.6f));
        for (int i = 0; i < Board.SIZE; i++) {
            // Vertical lines
            g2d.drawLine(i * UNIT_DIMENSION, 0, i * UNIT_DIMENSION, BOARD_DIMENSION);
            // Horizontal lines
            g2d.drawLine(0, i * UNIT_DIMENSION, BOARD_DIMENSION, i * UNIT_DIMENSION);
        }

        // 3. Draw Tengen and four corners star points
        int tengenAndStarsSize = 4;
        List<Point> tengenAndStars = List.of(
                Point.of(7, 7),
                Point.of(3, 3), Point.of(3, 11), Point.of(11, 11), Point.of(11, 3));
        tengenAndStars.forEach(point -> {
            Coordinate coordinate = Coordinate.fromPoint(point, UNIT_DIMENSION, 0);
            g2d.translate(coordinate.x(), coordinate.y());
            g2d.fillOval(-tengenAndStarsSize, -tengenAndStarsSize, tengenAndStarsSize * 2, tengenAndStarsSize * 2);
            g2d.translate(-coordinate.x(), -coordinate.y());
        });

        // 4. Draw a cursor tip
        if (cursorTip != null && BoardChecker.isOnBoard(cursorTip) && !state.isOver() && state.isSelfTurn()) {
            Coordinate coordinate = Coordinate.fromPoint(cursorTip, UNIT_DIMENSION, 0);
            if (state == null || BoardChecker.isEmpty(state.board(), cursorTip)) {
                g2d.setColor(java.awt.Color.GREEN);
            } else {
                g2d.setColor(java.awt.Color.RED);
            }
            g2d.setStroke(new BasicStroke(2));

            int size = 4;
            int offset = STONE_DIMENSION / 2 + 2;

            g2d.translate(coordinate.x(), coordinate.y());
            for (int i = 0; i < 4; i++) {
                g2d.drawLine(-offset, -offset, -offset + size, -offset);
                g2d.drawLine(-offset, -offset, -offset, -offset + size);
                g2d.rotate(Math.PI / 2);
            }
            g2d.translate(-coordinate.x(), -coordinate.y());
        }

        if (state == null) return;

        // 5. Draw stones
        SetUtils.forEachWithIndex(state.stoneSequence(), (index, stone) -> {
            Image image = stone.color() == Color.BLACK ? ImageAssets.getBlackStone() : ImageAssets.getWhiteStone();
            Coordinate coordinate = Coordinate.fromPoint(stone.point(), UNIT_DIMENSION, 0);
            g2d.translate(coordinate.x(), coordinate.y());
            g2d.drawImage(image, -STONE_DIMENSION / 2, -STONE_DIMENSION / 2, STONE_DIMENSION, STONE_DIMENSION, this);

            // Add an ordinal number to each stone
            if (stone.color() == Color.BLACK) {
                g2d.setColor(java.awt.Color.WHITE);
            } else {
                g2d.setColor(java.awt.Color.BLACK);
            }
            String text = String.valueOf(index + 1);
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            g2d.drawString(text, -textWidth / 2, 4);
            g2d.translate(-coordinate.x(), -coordinate.y());
        });

        // 6. Highlight last stone
        if (!state.stoneSequence().isEmpty()) {
            Coordinate coordinate = Coordinate.fromPoint(state.stoneSequence().getLast().point(), UNIT_DIMENSION, 0);

            g2d.setColor(java.awt.Color.RED);
            g2d.setStroke(new BasicStroke(2));

            int size = 4;
            int offset = STONE_DIMENSION / 2 + 2;

            g2d.translate(coordinate.x(), coordinate.y());
            for (int i = 0; i < 4; i++) {
                g2d.drawLine(-offset, -offset, -offset + size, -offset);
                g2d.drawLine(-offset, -offset, -offset, -offset + size);
                g2d.rotate(Math.PI / 2);
            }
            g2d.translate(-coordinate.x(), -coordinate.y());
        }

        g2d.translate(-OFFSET, -OFFSET);

        // 7. Draw game result
        if (state != null && state.isOver()) {
            String text = "游戏结束";
            g2d.setColor(java.awt.Color.ORANGE);
            if (state.winner() == Color.WHITE) {
                text = "白棋获胜";
                g2d.setColor(java.awt.Color.white);
            } else if (state.winner() == Color.BLACK) {
                text = "黑棋获胜";
                g2d.setColor(java.awt.Color.BLACK);
            }

            g2d.setFont(FontAssets.LXGWWenKaiMonoScreen.deriveFont(Font.BOLD, 64));
            // Calculate the text width so that it is centered
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            g2d.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 3);
        }
    }


    /**
     * Turn on all optimizations at once
     *
     * @param g2d
     */
    private void enableGraphicsOptimizations(Graphics2D g2d) {
        // Enable anti-aliasing to smooth edges of shapes
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Enable text anti-aliasing for smoother text rendering
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Improve overall rendering quality
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Improve color rendering quality
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        // Enable dithering to create smoother color transitions
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

        // Use bilinear interpolation for smoother image scaling
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Improve alpha interpolation quality for better transparency rendering
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        // Ensure stroke edges are rendered more accurately
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }


    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {

            private long time = 0;

            @Override
            public void mousePressed(MouseEvent e) {
                // Prevent JPanel from responding to mouse click events
                if (isEnabled()) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - time < 500) return;
                    time = currentTime;

                    Point point = Coordinate.of(e.getX(), e.getY()).toPoint(UNIT_DIMENSION, OFFSET);
                    Color color = state == null ? Color.BLACK : state.selfColor();
                    GomokuEventBus.getInstance().publish(new StonePlaceEvent(this, Stone.of(point, color)));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cursorTip = null;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cursorTip = null;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            private long time = 0;

            @Override
            public void mouseMoved(MouseEvent e) {
                if (isEnabled()) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - time < 50) return;
                    time = currentTime;
                    cursorTip = Coordinate.of(e.getX(), e.getY()).toPoint(UNIT_DIMENSION, OFFSET);
                    if (!BoardChecker.isOnBoard(cursorTip)) {
                        cursorTip = null;
                    }
                    repaint();
                }
            }
        });
    }

    private void subscribeToEvents() {

        subscribe(GameStartEvent.class, event -> {
            setEnabled(true);
            repaint();
        });

        subscribe(BoardUpdateEvent.class, event -> {
            this.state = event.getState();
            repaint();
        });

        subscribe(GameOverEvent.class, event -> repaint());
    }
}
