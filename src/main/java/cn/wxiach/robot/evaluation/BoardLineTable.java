package cn.wxiach.robot.evaluation;

import cn.wxiach.model.Board;
import cn.wxiach.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wxiach 2025/5/10
 */
public class BoardLineTable {

		public static final int BOARD_LINE_COUNT = 88;

		// 存储所有棋盘线（行、列、左斜线、右斜线）的索引，每条线由一个包含点索引的数组表示
		public final static List<int[]> BOARD_LINES = new ArrayList<>();

		/**
		 * 坐标到线索引的映射表
		 * 存储每个棋盘坐标点对应的行、列、左斜线和右斜线的索引值
		 */
		public final static LineIndices[] POINT_LINE_MAPPING = new LineIndices[Board.SIZE * Board.SIZE];


		/**
		 * 表示一个坐标点关联的四个方向线索引
		 *
		 * @param row           所在行索引
		 * @param column        所在列索引
		 * @param leftDiagonal  所在左斜线(↗)索引
		 * @param rightDiagonal 所在右斜线(↘)索引
		 */
		public record LineIndices(int row, int column, int leftDiagonal, int rightDiagonal) {
				/**
				 * 返回所有四个方向的线索引数组
				 *
				 * @return 包含行、列、左斜线、右斜线索引的列表
				 */
				public List<Integer> indices() {
						return List.of(row, column, leftDiagonal, rightDiagonal);
				}
		}

		static {
				initBoardLines();
				initPointLineMapping();
		}


		/**
		 * 初始化所有棋盘线索引
		 * <p>
		 * 该方法创建四种类型的棋盘线数组，总计 60 条线：
		 * 1. 水平方向线(→)：Board.SIZE条(15条)，每行一条，索引范围[0-14]
		 * 2. 垂直方向线(↓)：Board.SIZE条(15条)，每列一条，索引范围[15-29]
		 * 3. 左斜线方向(↗, 左上到右下)：2*Board.SIZE-1条(29条)，x+y值相同的点在同一条线上，索引范围[30-58]
		 * 4. 右斜线方向(↘, 左下到右上)：2*Board.SIZE-1条(29条)，x-y值相同的点在同一条线上，索引范围[59-87]
		 * <p>
		 * 每条线由一个整数数组表示，数组中的每个元素是棋盘上某个点的一维索引值。
		 * 所有线按顺序存储在BOARD_LINES全局列表中，供后续棋型评估使用。
		 */
		private static void initBoardLines() {
				// 水平方向线(→)
				for (int y = 0; y < Board.SIZE; y++) {
						List<Integer> linePoints = new ArrayList<>();
						for (int x = 0; x < Board.SIZE; x++) {
								linePoints.add(Board.index(Point.of(x, y)));
						}
						BOARD_LINES.add(linePoints.stream().mapToInt(Integer::intValue).toArray());
				}

				// 垂直方向线(↓)
				for (int x = 0; x < Board.SIZE; x++) {
						List<Integer> linePoints = new ArrayList<>();
						for (int y = 0; y < Board.SIZE; y++) {
								linePoints.add(Board.index(Point.of(x, y)));
						}
						BOARD_LINES.add(linePoints.stream().mapToInt(Integer::intValue).toArray());
				}

				// 左斜线方向(↗): 特征是x+y值相同的点在同一条线上
				// 遍历所有可能的x+y值: 从0到(Board.SIZE-1)*2
				for (int sum = 0; sum <= (Board.SIZE - 1) * 2; sum++) {
						List<Integer> linePoints = new ArrayList<>();
						for (int x = 0; x < Board.SIZE; x++) {
								int y = sum - x;
								if (y >= 0 && y < Board.SIZE) {
										linePoints.add(Board.index(Point.of(x, y)));
								}
						}
						// 只添加非空线(至少有一个点)
						if (!linePoints.isEmpty()) {
								BOARD_LINES.add(linePoints.stream().mapToInt(Integer::intValue).toArray());
						}
				}

				// 右斜线方向(↘): 特征是x-y值相同的点在同一条线上
				// 遍历所有可能的x-y值: 从-(Board.SIZE-1)到(Board.SIZE-1)
				for (int diff = -(Board.SIZE - 1); diff <= Board.SIZE - 1; diff++) {
						List<Integer> linePoints = new ArrayList<>();
						for (int x = 0; x < Board.SIZE; x++) {
								int y = x - diff;
								if (y >= 0 && y < Board.SIZE) {
										linePoints.add(Board.index(Point.of(x, y)));
								}
						}
						// 只添加非空线(至少有一个点)
						if (!linePoints.isEmpty()) {
								BOARD_LINES.add(linePoints.stream().mapToInt(Integer::intValue).toArray());
						}
				}
		}

		/**
		 * 初始化每个棋盘点对应的四个方向线索引映射
		 * <p>
		 * 计算公式说明：
		 * 1. 行索引 = point.y() + offset[0]
		 * - y坐标直接对应行号，加上行的起始偏移量0
		 * <p>
		 * 2. 列索引 = point.x() + offset[1]
		 * - x坐标直接对应列号，加上列的起始偏移量15
		 * - 列的起始偏移量15等于Board.SIZE(15)，表示前15条是行
		 * <p>
		 * 3. 左斜线索引 = point.x() + point.y() + offset[2]
		 * - 左斜线(↘)的特征是x+y值相同的点在同一条线上
		 * - 加上左斜线的起始偏移量30，表示前30条是行和列
		 * <p>
		 * 4. 右斜线索引 = point.x() - point.y() + Board.SIZE - 1 + offset[3]
		 * - 右斜线(↗)的特征是x-y值相同的点在同一条线上
		 * - 加上(Board.SIZE-1)进行标准化，确保索引为非负数
		 * - 加上右斜线的起始偏移量59，表示前面已有59条线
		 */
		private static void initPointLineMapping() {
				int[] offest = new int[]{0, 15, 30, 59};
				for (int index = 0; index < POINT_LINE_MAPPING.length; index++) {
						Point point = Board.point(index);
						POINT_LINE_MAPPING[index] = new LineIndices(
										point.y() + offest[0],                          // 行索引
										point.x() + offest[1],                               // 列索引
										point.x() + point.y() + offest[2],                   // 左斜线索引
										point.x() - point.y() + Board.SIZE - 1 + offest[3]   // 右斜线索引
						);
				}
		}
}
