package cn.wxiach.utils;

import cn.wxiach.model.Board;
import cn.wxiach.model.Color;

public class BoardUtils {

		public static int countStones(Board board) {
				int cnt = 0;
				for (int i = 0; i < board.length(); i++) {
						if (board.get(i) != Color.EMPTY.value()) cnt++;
				}
				return cnt;
		}
}
