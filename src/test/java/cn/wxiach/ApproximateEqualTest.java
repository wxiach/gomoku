package cn.wxiach;

import org.junit.jupiter.api.Test;

import static cn.wxiach.robot.search.IterativeDeepeningSearch.approximateEqual;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ApproximateEqualTest {

    @Test
    public void testApproximateEqual_withExactMatch() {
        assertTrue(approximateEqual(10.0, 10.0, 1.1));
    }

    @Test
    public void testApproximateEqual_withSmallDifference() {
        assertTrue(approximateEqual(10.5, 10.0, 1.1));
    }

    @Test
    public void testApproximateEqual_withLargeDifference() {
        assertFalse(approximateEqual(12.0, 10.0, 1.1));
    }

    @Test
    public void testApproximateEqual_withZeroThreshold() {
        assertFalse(approximateEqual(10.0, 10.0, 0.0));
    }

    @Test
    public void testApproximateEqual_withNegativeValue() {
        assertTrue(approximateEqual(-10.0, -10.0, 1.1));
    }

    @Test
    public void testApproximateEqual_withZeroB() {
        assertTrue(approximateEqual(0.005, 0.0, 2));
    }
}
