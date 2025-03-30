package cn.wxiach.core.ai.pattern.feature;

import cn.wxiach.core.ai.pattern.Pattern;
import cn.wxiach.model.Color;
import cn.wxiach.model.Piece;

import java.util.List;

public interface PatternDetector<T> {

    Pattern<T> detect(FeatureLine line, Color color);

    List<Pattern<T>> detect(char[][] board, Piece piece);

    List<Pattern<T>> detect(char[][] board, Color color);
}
