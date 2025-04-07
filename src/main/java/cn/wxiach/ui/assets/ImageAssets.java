package cn.wxiach.ui.assets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ImageAssets {

    public static final String GOMOKU_BOARD_IMAGE_PATH = "/images/gomoku-board.png";
    public static final String BLACK_STONE_IMAGE_PATH = "/images/black-stone.png";
    public static final String WHITE_STONE_IMAGE_PATH = "/images/white-stone.png";

    private static final BufferedImage boardImage;
    private static final BufferedImage blackStone;
    private static final BufferedImage whiteStone;


    static {
        URL boardImageResource = ImageAssets.class.getResource(GOMOKU_BOARD_IMAGE_PATH);
        URL blackStoneResource = ImageAssets.class.getResource(BLACK_STONE_IMAGE_PATH);
        URL whiteStoneResource = ImageAssets.class.getResource(WHITE_STONE_IMAGE_PATH);
        try {
            boardImage = ImageIO.read(Objects.requireNonNull(boardImageResource));
            blackStone = ImageIO.read(Objects.requireNonNull(blackStoneResource));
            whiteStone = ImageIO.read(Objects.requireNonNull(whiteStoneResource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage getBoardImage() {
        return boardImage;
    }

    public static BufferedImage getBlackStone() {
        return blackStone;
    }

    public static BufferedImage getWhiteStone() {
        return whiteStone;
    }
}
