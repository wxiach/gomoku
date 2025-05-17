package cn.wxiach.robot.evaluation;

import cn.wxiach.model.Stone;

public interface BoardEvaluator {

		/**
		 * 增量式评估当前棋盘的分数
		 *
		 * @param stone 最新落子/撤子
		 */
		void evaluate(Stone stone);
}
