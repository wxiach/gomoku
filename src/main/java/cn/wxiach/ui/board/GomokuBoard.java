package cn.wxiach.ui.board;

import cn.wxiach.core.rule.BoardCheck;
import cn.wxiach.core.store.GomokuStore;
import cn.wxiach.event.EventBusAware;
import cn.wxiach.event.GomokuEventBus;
import cn.wxiach.event.SubscriberPriority;
import cn.wxiach.event.types.BoardUpdateEvent;
import cn.wxiach.event.types.GameOverEvent;
import cn.wxiach.event.types.GameStartEvent;
import cn.wxiach.event.types.StonePlaceEvent;
import cn.wxiach.model.Board;
import cn.wxiach.model.Color;
import cn.wxiach.model.Point;
import cn.wxiach.model.Stone;
import cn.wxiach.ui.common.assets.FontAssets;
import cn.wxiach.ui.common.assets.ImageAssets;
import cn.wxiach.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

public class GomokuBoard extends JPanel implements EventBusAware {

    public static final int UNIT_DIMENSION = 40;
    public static final int STONE_DIMENSION = 30;
    // OFFSET 等于 UNIT_SIZE 既好计算又好看
    public static final int OFFSET = UNIT_DIMENSION;
    private static final int BOARD_DIMENSION = (Board.SIZE - 1) * UNIT_DIMENSION;
    private GomokuStore store;

    private Point cursorTip;

    public GomokuBoard() {
        setPreferredSize(new Dimension(BOARD_DIMENSION + (2 * OFFSET), BOARD_DIMENSION + (2 * OFFSET)));
        setEnabled(false);
        addMouseListeners();
        subscribeToEvents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // 调用父类方法确保其他组件正确绘制。
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 1. 一次性开启所有优化
        enableGraphicsOptimizations(g2d);

        g2d.drawImage(ImageAssets.getBoardImage(), 0, 0, getWidth(), getHeight(), this);

        // 2. 绘制棋盘坐标
        g2d.setFont(FontAssets.LXGWWenKaiMonoScreen.deriveFont(Font.BOLD, 14f));
        g2d.setColor(java.awt.Color.BLACK);

        char[] verticalCoordinateTable = "ABCDEFGHJKLMNOP".toCharArray();

        for (int i = 0; i < Board.SIZE; i++) {
            // 水平坐标
            String horizontalCoordinateValue = String.valueOf(i + 1);
            int textWidth = g2d.getFontMetrics().stringWidth(horizontalCoordinateValue);
            g2d.drawString(horizontalCoordinateValue, UNIT_DIMENSION - textWidth - 16, UNIT_DIMENSION + i * UNIT_DIMENSION + 4);
            // 垂直坐标
            String verticalCoordinateValue = String.valueOf(verticalCoordinateTable[i]);
            g2d.drawString(verticalCoordinateValue, UNIT_DIMENSION + i * UNIT_DIMENSION - 5, 24);
        }

        // 步骤3到6将在棋盘上完成，
        // 因此将绘制原点移动到棋盘的左上边界。
        g2d.translate(OFFSET, OFFSET);

        // 3. 绘制棋盘线，边缘留有边距。
        g2d.setStroke(new BasicStroke(1.1f));
        for (int i = 0; i < Board.SIZE; i++) {
            // 垂直线
            g2d.drawLine(i * UNIT_DIMENSION, 0, i * UNIT_DIMENSION, BOARD_DIMENSION);
            // 水平线
            g2d.drawLine(0, i * UNIT_DIMENSION, BOARD_DIMENSION, i * UNIT_DIMENSION);
        }

        // 3. 绘制天元和四角星位
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

        // 4. 绘制光标提示
        if (cursorTip != null && BoardCheck.isOnBoard(cursorTip) && !store.getGameState().isOver() && store.getTurnState().isHumanTurn()) {
            Coordinate coordinate = Coordinate.fromPoint(cursorTip, UNIT_DIMENSION, 0);
            if (store == null || BoardCheck.isEmpty(store.getBoardState().board(), cursorTip)) {
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

        if (store == null) return;

        // 5. 绘制棋子
        CollectionUtils.forEachWithIndex(store.getBoardState().stoneSequence(), (index, stone) -> {
            Image image = stone.color() == Color.BLACK ? ImageAssets.getBlackStone() : ImageAssets.getWhiteStone();
            Coordinate coordinate = Coordinate.fromPoint(stone.point(), UNIT_DIMENSION, 0);
            g2d.translate(coordinate.x(), coordinate.y());
            g2d.drawImage(image, -STONE_DIMENSION / 2, -STONE_DIMENSION / 2, STONE_DIMENSION, STONE_DIMENSION, this);

            // 为每个棋子添加序号
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

        // 6. 高亮最后一个棋子
        if (!store.getBoardState().stoneSequence().isEmpty()) {
            Coordinate coordinate = Coordinate.fromPoint(store.getBoardState().stoneSequence().getLast().point(), UNIT_DIMENSION, 0);

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

        // 7. 绘制游戏结果
        if (store != null && store.getGameState().isOver()) {
            String text = "你认输了";
            g2d.setColor(java.awt.Color.RED);
            if (store.getGameState().getWinner() == Color.WHITE) {
                text = "白棋获胜";
                g2d.setColor(java.awt.Color.white);
            } else if (store.getGameState().getWinner() == Color.BLACK) {
                text = "黑棋获胜";
                g2d.setColor(java.awt.Color.BLACK);
            }

            g2d.setFont(FontAssets.LXGWWenKaiMonoScreen.deriveFont(Font.BOLD, 64));
            // 计算文本宽度以使其居中
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            g2d.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 3);
        }
    }


    /**
     * 一次性开启所有优化
     *
     * @param g2d
     */
    private void enableGraphicsOptimizations(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }


    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {

            private long time = 0;

            @Override
            public void mousePressed(MouseEvent e) {
                // 防止 JPanel 响应鼠标点击事件
                if (isEnabled()) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - time < 500) return;
                    time = currentTime;

                    Point point = Coordinate.of(e.getX(), e.getY()).toPoint(UNIT_DIMENSION, OFFSET);
                    Color color = store == null ? Color.BLACK : store.getTurnState().getHumanStoneColor();
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
                    if (!BoardCheck.isOnBoard(cursorTip)) {
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
            this.store = event.getStore();
            repaint();
        });

        subscribe(GameOverEvent.class, event -> repaint());
    }

    @Override
    public SubscriberPriority defaultSubscriberPriority() {
        return SubscriberPriority.UI;
    }
}
