package cn.wxiach.ai.search;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;

public record SearchContext(Board board, Color color, int depth) {
}
