package cn.wxiach.core.utils;

import java.util.Arrays;

public class BoardUtils {

    public static char[][] deepCopy2D(char[][] arr) {
        return Arrays.stream(arr).map(char[]::clone).toArray(char[][]::new);
    }
}
