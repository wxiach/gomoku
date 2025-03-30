package cn.wxiach.ui.assets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ImageAssets {

    public static final String GOMOKU_BOARD_IMAGE_PATH = "/images/gomoku-board.png";
    public static final String BLACK_PIECE_IMAGE_PATH = "/images/black-piece.png";
    public static final String WHITE_PIECE_IMAGE_PATH = "/images/white-piece.png";

    private static final BufferedImage boardImage;
    private static final BufferedImage blackPiece;
    private static final BufferedImage whitePiece;


    static {
        URL boardImageResource = ImageAssets.class.getResource(GOMOKU_BOARD_IMAGE_PATH);
        URL blackPieceResource = ImageAssets.class.getResource(BLACK_PIECE_IMAGE_PATH);
        URL whitePieceResource = ImageAssets.class.getResource(WHITE_PIECE_IMAGE_PATH);
        try {
            boardImage = ImageIO.read(Objects.requireNonNull(boardImageResource));
            blackPiece = ImageIO.read(Objects.requireNonNull(blackPieceResource));
            whitePiece = ImageIO.read(Objects.requireNonNull(whitePieceResource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage getBoardImage() {
        return boardImage;
    }

    public static BufferedImage getBlackPiece() {
        return blackPiece;
    }

    public static BufferedImage getWhitePiece() {
        return whitePiece;
    }
}
