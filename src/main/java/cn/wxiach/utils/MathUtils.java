package cn.wxiach.utils;

/**
 * @author wxiach 2025/5/7
 */
public class MathUtils {

    /**
     * Determines if two double values are approximately equal within a given threshold.
     *
     * @param a         The first double value.
     * @param b         The second double value.
     * @param threshold The threshold for approximation. Must be greater than 0.
     * @return True if the values are approximately equal, false otherwise.
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
