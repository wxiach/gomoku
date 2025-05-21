package cn.wxiach.utils;

/**
 * @author wxiach 2025/5/7
 */
public class MathUtils {

    /**
     * 判断两个 double 值在给定阈值范围内是否近似相等。
     *
     * @param a         第一个 double 值。
     * @param b         第二个 double 值。
     * @param threshold 近似判断的阈值。必须大于 0。
     * @return 如果两个值近似相等则返回 true，否则返回 false。
     */
    public static boolean approximateEqual(double a, double b, double threshold) {
        if (b == 0) {
            b = 0.01;
        }
        if (b >= 0) {
            return (a >= b / threshold) && (a <= b * threshold);
        } else {
            return (a >= b * threshold) && (a <= b / threshold);
        }
    }
}
