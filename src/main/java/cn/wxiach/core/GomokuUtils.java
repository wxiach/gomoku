package cn.wxiach.core;

import java.util.Arrays;

public class GomokuUtils {

    public static char[][] deepCopy2D(char[][] arr) {
        return Arrays.stream(arr).map(char[]::clone).toArray(char[][]::new);
    }

}
