package cn.wxiach.ai.pattern;

import java.util.Collection;

public interface ShapeDetector {

    Collection<Pattern> detect(char[][] board);

    Collection<Pattern> detect(char[][] board, int x, int y);
}
