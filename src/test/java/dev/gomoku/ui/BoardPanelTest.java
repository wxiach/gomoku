package dev.gomoku.ui;

import dev.gomoku.model.Board;
import dev.gomoku.model.Stone;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 验证无图形环境下的棋盘绘制，以及点击位置和回合限制。
 *
 * @author wxiach@gmail.com 2026/11/02
 */
class BoardPanelTest {
    @Test
    void clicksRespectTurnAndOccupiedIntersections() throws Exception {
        AtomicInteger clicks = new AtomicInteger();
        Board board = new Board();
        board.place(7, 7, Stone.BLACK);
        SwingUtilities.invokeAndWait(() -> {
            BoardPanel panel = new BoardPanel(board, (row, col) -> clicks.incrementAndGet());
            panel.setSize(panel.getPreferredSize());
            panel.update(board, true);
            click(panel, 320, 323);
            assertEquals(0, clicks.get());
            click(panel, 362, 323);
            assertEquals(1, clicks.get());
            click(panel, 2, 2);
            assertEquals(1, clicks.get());
            panel.update(board, false);
            click(panel, 362, 323);
            assertEquals(1, clicks.get());
        });
    }

    @Test
    void rendersBoardAndWinningLineInHeadlessMode() throws Exception {
        Board board = new Board();
        board.place(8, 6, Stone.WHITE);
        board.place(8, 7, Stone.WHITE);
        for (int col = 4; col <= 8; col++) {
            board.place(7, col, Stone.BLACK);
        }
        Board beforeWin = board.copy();
        beforeWin.undo();
        BufferedImage beforeImage = new BufferedImage(640, 640, BufferedImage.TYPE_INT_RGB);
        BufferedImage image = new BufferedImage(640, 640, BufferedImage.TYPE_INT_RGB);
        SwingUtilities.invokeAndWait(() -> {
            BoardPanel panel = new BoardPanel(beforeWin, (row, col) -> fail("绘制棋盘时不应触发落子"));
            panel.setSize(panel.getPreferredSize());
            Graphics2D beforeGraphics = beforeImage.createGraphics();
            panel.paint(beforeGraphics);
            beforeGraphics.dispose();
            panel.update(board, false);
            Graphics2D g = image.createGraphics();
            g.setColor(java.awt.Color.decode("#f7f5ec"));
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            panel.paint(g);
            panel.setShowNumbers(false);
            panel.paint(g);
            panel.setShowNumbers(true);
            panel.paint(g);
            g.dispose();
        });
        assertNotEquals(image.getRGB(0, 0), image.getRGB(25, 25));

        // 比较胜负发生前后同一颗白棋，边框和序号都不应随获胜画笔发生变化。
        int whiteX = 278;
        int whiteY = 365;
        int radius = 20;
        for (int y = whiteY - radius; y <= whiteY + radius; y++) {
            for (int x = whiteX - radius; x <= whiteX + radius; x++) {
                assertEquals(beforeImage.getRGB(x, y), image.getRGB(x, y), "结束后不应加粗棋子边框");
            }
        }

        File preview = new File("target/board-preview.png");
        assertTrue(ImageIO.write(image, "png", preview));
    }

    private static void click(BoardPanel panel, int x, int y) {
        panel.dispatchEvent(new MouseEvent(panel, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, x, y, 1, false, MouseEvent.BUTTON1));
    }
}
