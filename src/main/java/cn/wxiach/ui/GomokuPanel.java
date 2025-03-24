package cn.wxiach.ui;

import cn.wxiach.core.BoardManager;
import cn.wxiach.model.Piece;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.support.PiecePlacedEvent;
import cn.wxiach.event.support.HumanClickEvent;

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
    private int[][] board;

    public GomokuPanel() {
        int width = BoardManager.BOARD_SIZE * BOARD_PANEL_UNIT_SIZE;
        int height = BoardManager.BOARD_SIZE * BOARD_PANEL_UNIT_SIZE;
        setPreferredSize(new Dimension(width, height));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = Math.round((float) e.getX() / BOARD_PANEL_UNIT_SIZE - 1);
                int y = Math.round((float) e.getY() / BOARD_PANEL_UNIT_SIZE - 1);
                GomokuEventBus.getInstance().publish(new HumanClickEvent(this, x, y));
            }
        });

        GomokuEventBus.getInstance().subscribe(PiecePlacedEvent.class, event -> {
            this.board = event.getLatestBord();
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
        for (int i = 1; i < BoardManager.BOARD_SIZE; i++) {
            // Vertical lines
            g.drawLine(i * BOARD_PANEL_UNIT_SIZE, BOARD_PANEL_UNIT_SIZE,
                    i * BOARD_PANEL_UNIT_SIZE, (BoardManager.BOARD_SIZE - 1) * BOARD_PANEL_UNIT_SIZE);
            // Horizontal lines
            g.drawLine(BOARD_PANEL_UNIT_SIZE, i * BOARD_PANEL_UNIT_SIZE,
                    (BoardManager.BOARD_SIZE - 1) * BOARD_PANEL_UNIT_SIZE, i * BOARD_PANEL_UNIT_SIZE);
        }

        // Draw Tengen and four corners star points
        int[][] fourPointPosition = {
                {7 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2), 7 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2)},
                {4 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2), 4 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2)},
                {4 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2), 11 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2)},
                {11 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2), 4 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2)},
                {11 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2), 11 * BOARD_PANEL_UNIT_SIZE - (BOARD_POINT_SIZE / 2)},
        };

        for (int[] point : fourPointPosition) {
            g.fillRect(point[0], point[1], BOARD_POINT_SIZE, BOARD_POINT_SIZE);
        }

        // Draw pieces
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
            for (int i = 0; i < BoardManager.BOARD_SIZE; i++) {
                for (int j = 0; j < BoardManager.BOARD_SIZE; j++) {
                    int pieceColor = board[i][j];

                    Image pieceImage = null;
                    if (pieceColor == Piece.Color.BLACK.getValue()) {
                        pieceImage = blackPiece;
                    }
                    if (pieceColor == Piece.Color.WHITE.getValue()) {
                        pieceImage = whitePiece;
                    }

                    if (pieceImage == null) continue;

                    g.drawImage(
                            pieceImage,
                            (i + 1) * BOARD_PANEL_UNIT_SIZE - (PIECE_SIZE / 2),
                            (j + 1) * BOARD_PANEL_UNIT_SIZE - (PIECE_SIZE / 2),
                            PIECE_SIZE,
                            PIECE_SIZE,
                            this
                    );

                }
            }
        }

    }
}
