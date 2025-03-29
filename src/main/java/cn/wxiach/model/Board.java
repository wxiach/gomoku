package cn.wxiach.model;

import java.util.List;

public interface Board {

    int BOARD_SIZE = 15;

    List<Piece> pieces();

    char[][] board();
}
