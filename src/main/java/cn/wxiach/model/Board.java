package cn.wxiach.model;

import java.util.Arrays;

/**
 * 表示一个 15x15 的五子棋游戏棋盘。
 * <p>
 * 棋盘使用一维字符数组来存储游戏状态以获得最佳性能。
 * 每个位置可以为空或被黑色/白色棋子占据。
 * 坐标系以左上角为原点 (0,0)。
 */
public class Board {

		public static final int SIZE = 15;
		private final char[] board;

		/**
		 * 创建一个空的游戏棋盘，所有位置初始化为 Color.EMPTY。
		 */
		public Board() {
				this.board = new char[SIZE * SIZE];
				Arrays.fill(board, Color.EMPTY.value());
		}

		/**
		 * 拷贝构造函数
		 *
		 * @param other 要拷贝的棋盘对象
		 */
		public Board(Board other) {
				// 同一个类的不同实例可以访问彼此的私有成员
				this.board = Arrays.copyOf(other.board, other.board.length);
		}

		/**
		 * 获取指定索引位置的棋盘内容。
		 *
		 * @param index 一维数组索引
		 * @return 该位置的棋盘状态，表示为字符（空、黑棋或白棋）
		 */
		public char get(int index) {
				return board[index];
		}

		/**
		 * 获取棋盘的总长度（即棋盘格子总数）。
		 *
		 * @return 棋盘的总格子数，等于 SIZE * SIZE
		 */
		public int length() {
				return board.length;
		}

		/**
		 * 创建当前棋盘状态的深拷贝。
		 * 使用System.arraycopy以获得最佳性能。
		 */
		public Board copy() {
				return new Board(this);
		}


		/**
		 * 在棋盘上放置一颗棋子。这是下棋的主要方法。
		 */
		public void makeMove(Stone stone) {
				board[index(stone.point())] = stone.color().value();
		}

		/**
		 * 从棋盘上移除一颗棋子，将该位置恢复为空状态。
		 * 用于撤销移动操作。
		 */
		public void undoMove(Stone stone) {
				board[index(stone.point())] = Color.EMPTY.value();
		}

		/**
		 * 获取指定索引位置的棋子信息。
		 *
		 * @param index 一维数组索引
		 * @return 对应位置的棋子对象，包含坐标和颜色信息
		 */
		public Stone stone(int index) {
				return Stone.of(point(index), Color.of(this.board[index]));
		}

		/**
		 * 获取指定坐标位置的棋子信息。
		 *
		 * @param point 二维棋盘坐标
		 * @return 对应位置的棋子对象，包含坐标和颜色信息
		 */
		public Stone stone(Point point) {
				return Stone.of(point, Color.of(this.board[index(point)]));
		}

		/**
		 * 将一维数组索引转换为二维棋盘坐标。
		 * <p>
		 * 转换遵循公式：x = index % SIZE, y = index / SIZE
		 *
		 * @param index 一维数组索引
		 * @return 对应的二维棋盘坐标
		 */
		public static Point point(int index) {
				return Point.of(index % SIZE, index / SIZE);
		}

		/**
		 * 将二维棋盘坐标转换为一维数组索引。
		 * <p>
		 * 转换遵循公式：index = y * SIZE + x
		 * 这种映射方式确保了棋盘位置的高效存储和访问。
		 *
		 * @param point 二维棋盘坐标
		 * @return 对应于内部数组的索引
		 */
		public static int index(Point point) {
				return point.y() * SIZE + point.x();
		}
}


