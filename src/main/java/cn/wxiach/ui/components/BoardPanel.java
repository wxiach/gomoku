package cn.wxiach.ui.components;

import cn.wxiach.core.state.support.GameStateReadable;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.BoardUpdateEvent;
import cn.wxiach.event.support.GameOverEvent;
import cn.wxiach.event.support.GameStartEvent;
import cn.wxiach.event.support.PiecePlaceEvent;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;
import cn.wxiach.ui.assets.FontAssets;
import cn.wxiach.ui.assets.ImageAssets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {

    public static final int BOARD_PANEL_UNIT_SIZE = 40;
    public static final int BOARD_POINT_SIZE = 6;
    public static final int PIECE_SIZE = 30;

    private static final int width = (Board.BOARD_SIZE + 1) * BOARD_PANEL_UNIT_SIZE;
    private static final int height = (Board.BOARD_SIZE + 1) * BOARD_PANEL_UNIT_SIZE;

    private GameStateReadable state;

    public BoardPanel() {
        setPreferredSize(new Dimension(width, height));
        setEnabled(false);
        addMouseListeners();
        subscribeToEvents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Calling the super method ensures that other components are painted correctly.
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Turn on all optimizations at once
        enableGraphicsOptimizations(g2d);

        g2d.drawImage(ImageAssets.getBoardImage(), 0, 0, getWidth(), getHeight(), this);

        // Draw board lines with a margin around the edges.
        for (int i = 1; i <= Board.BOARD_SIZE; i++) {
            // Vertical lines
            g2d.drawLine(i * BOARD_PANEL_UNIT_SIZE, BOARD_PANEL_UNIT_SIZE,
                    i * BOARD_PANEL_UNIT_SIZE, (Board.BOARD_SIZE) * BOARD_PANEL_UNIT_SIZE);
            // Horizontal lines
            g2d.drawLine(BOARD_PANEL_UNIT_SIZE, i * BOARD_PANEL_UNIT_SIZE,
                    (Board.BOARD_SIZE) * BOARD_PANEL_UNIT_SIZE, i * BOARD_PANEL_UNIT_SIZE);
        }

        // Draw Tengen and four corners star points
        int[][] fourPointPosition = {
                {8 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2), 8 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2)},
                {4 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2), 4 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2)},
                {4 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2), 12 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2)},
                {12 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2), 4 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2)},
                {12 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2), 12 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2)},
        };

        for (int[] point : fourPointPosition) {
            g2d.fillRect(point[0], point[1], BOARD_POINT_SIZE, BOARD_POINT_SIZE);
        }

        // Draw pieces
        if (state != null) {
            state.pieces().forEach(piece -> {
                Image image = piece.color() == Color.BLACK ? ImageAssets.getBlackPiece() : ImageAssets.getWhitePiece();
                int x = (piece.point().x() + 1) * BOARD_PANEL_UNIT_SIZE - (PIECE_SIZE / 2);
                int y = (piece.point().y() + 1) * BOARD_PANEL_UNIT_SIZE - (PIECE_SIZE / 2);
                g2d.drawImage(image, x, y, PIECE_SIZE, PIECE_SIZE, this);
            });
        }

        // Todo Highlight last piece

        if (state != null && state.isOver()) {
            g2d.setFont(FontAssets.LXGWWenKaiMonoScreen.deriveFont(Font.BOLD, 64));
            g2d.setColor(java.awt.Color.BLACK);
            String text;
            if (state.winner() == Color.EMPTY) {
                text = "游戏结束！";
            } else {
                String winner = state.winner() == Color.WHITE ? "白棋" : "黑棋";
                text = String.format("%s获胜！", winner);
            }

            // Calculate the text width so that it is centered
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            g2d.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 3);
        }
    }


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
                    if (currentTime - time < 1000) {
                        return;
                    }
                    time = currentTime;
                    int x = Math.round((float) e.getX() / BOARD_PANEL_UNIT_SIZE - 1);
                    int y = Math.round((float) e.getY() / BOARD_PANEL_UNIT_SIZE - 1);

                    Piece piece;
                    if (state == null) {
                        piece = Piece.of(x, y, Color.BLACK);
                    } else {
                        piece = Piece.of(x, y, state.selfColor());
                    }
                    GomokuEventBus.getInstance().publish(new PiecePlaceEvent(this, piece));
                }
            }
        });
    }

    private void subscribeToEvents() {

        GomokuEventBus.getInstance().subscribe(GameStartEvent.class, event -> {
            setEnabled(true);
        });

        GomokuEventBus.getInstance().subscribe(BoardUpdateEvent.class, event -> {
            this.state = event.getState();
            // Todo repaint rectangle
            repaint();
        });

        GomokuEventBus.getInstance().subscribe(GameOverEvent.class, event -> {
            repaint();
        });
    }
}
