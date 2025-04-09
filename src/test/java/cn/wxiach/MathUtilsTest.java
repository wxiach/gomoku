package cn.wxiach;

import cn.wxiach.core.utils.MathUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MathUtilsTest {

    @Test
    public void testApproximateEqual_withExactMatch() {
        assertTrue(MathUtils.approximateEqual(10.0, 10.0, 1.1));
    }

    @Test
    public void testApproximateEqual_withSmallDifference() {
        assertTrue(MathUtils.approximateEqual(10.5, 10.0, 1.1));
    }

    @Test
    public void testApproximateEqual_withLargeDifference() {
        assertFalse(MathUtils.approximateEqual(12.0, 10.0, 1.1));
    }

    @Test
    public void testApproximateEqual_withZeroThreshold() {
        assertFalse(MathUtils.approximateEqual(10.0, 10.0, 0.0));
    }

    @Test
    public void testApproximateEqual_withNegativeValue() {
        assertTrue(MathUtils.approximateEqual(-10.0, -10.0, 1.1));
    }

    @Test
    public void testApproximateEqual_withZeroB() {
        assertTrue(MathUtils.approximateEqual(0.005, 0.0, 2));
    }
}
