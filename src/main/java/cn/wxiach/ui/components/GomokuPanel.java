package cn.wxiach.ui.components;

import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.BoardUpdateEvent;
import cn.wxiach.event.support.HumanClickEvent;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class GomokuPanel extends JPanel {

    public static final int BOARD_PANEL_UNIT_SIZE = 40;
    public static final int BOARD_POINT_SIZE = 6;
    public static final int PIECE_SIZE = 30;

    public static final String GOMOKU_BOARD_IMAGE_PATH = "/gomoku-board.png";
    public static final String BLACK_PIECE_IMAGE_PATH = "/black-piece.png";
    public static final String WHITE_PIECE_IMAGE_PATH = "/white-piece.png";

    private Image gomokuBoardImage;
    private Image blackPiece;
    private Image whitePiece;
    private Board board;

    public GomokuPanel() {
        int width = (Board.BOARD_SIZE + 1) * BOARD_PANEL_UNIT_SIZE;
        int height = (Board.BOARD_SIZE + 1) * BOARD_PANEL_UNIT_SIZE;
        setPreferredSize(new Dimension(width, height));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = Math.round((float) e.getX() / BOARD_PANEL_UNIT_SIZE - 1);
                int y = Math.round((float) e.getY() / BOARD_PANEL_UNIT_SIZE - 1);
                GomokuEventBus.getInstance().publish(new HumanClickEvent(this, Point.of(x, y)));
            }
        });

        GomokuEventBus.getInstance().subscribe(BoardUpdateEvent.class, event -> {
            this.board = event.getBoard();
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Calling the super method ensures that other components are painted correctly.
        super.paintComponent(g);

        if (gomokuBoardImage == null) {
            URL resource = getClass().getResource(GOMOKU_BOARD_IMAGE_PATH);
            try {
                gomokuBoardImage = ImageIO.read(Objects.requireNonNull(resource));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        g.drawImage(gomokuBoardImage, 0, 0, getWidth(), getHeight(), this);

        // Draw board lines with a margin around the edges.
        for (int i = 1; i <= Board.BOARD_SIZE; i++) {
            // Vertical lines
            g.drawLine(i * BOARD_PANEL_UNIT_SIZE, BOARD_PANEL_UNIT_SIZE,
                    i * BOARD_PANEL_UNIT_SIZE, (Board.BOARD_SIZE) * BOARD_PANEL_UNIT_SIZE);
            // Horizontal lines
            g.drawLine(BOARD_PANEL_UNIT_SIZE, i * BOARD_PANEL_UNIT_SIZE,
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
            g.fillRect(point[0], point[1], BOARD_POINT_SIZE, BOARD_POINT_SIZE);
        }

        // Load pieces image resources
        if (whitePiece == null || blackPiece == null) {
            URL blackPieceResource = getClass().getResource(BLACK_PIECE_IMAGE_PATH);
            URL whitePieceResource = getClass().getResource(WHITE_PIECE_IMAGE_PATH);
            try {
                blackPiece = ImageIO.read(Objects.requireNonNull(blackPieceResource));
                whitePiece = ImageIO.read(Objects.requireNonNull(whitePieceResource));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Draw pieces
        if (board != null) {
            board.pieces().forEach(piece -> {
                Image image = piece.color() == Color.BLACK ? blackPiece : whitePiece;
                int x = (piece.point().x() + 1) * BOARD_PANEL_UNIT_SIZE - (PIECE_SIZE / 2);
                int y = (piece.point().y() + 1) * BOARD_PANEL_UNIT_SIZE - (PIECE_SIZE / 2);
                g.drawImage(image, x, y, PIECE_SIZE, PIECE_SIZE, this);
            });
        }

        // Todo Highlight last piece
    }
}
