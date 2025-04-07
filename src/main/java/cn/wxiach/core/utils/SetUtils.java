package cn.wxiach.core.utils;

import java.util.LinkedHashSet;
import java.util.function.BiConsumer;

public class SetUtils {

    public static <T> void forEachWithIndex(LinkedHashSet<T> set, BiConsumer<Integer, T> consumer) {
        int index = 0;
        for (T element : set) {
            consumer.accept(index++, element);
        }
    }

}
