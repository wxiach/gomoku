package cn.wxiach.utils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.BiConsumer;

public class CollectionUtils {

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !collection.isEmpty();
    }

    /**
     * 遍历 LinkedHashSet，并为每个元素及其索引应用一个 consumer。
     *
     * @param set      要遍历的集合
     * @param consumer 处理索引和元素的操作
     * @param <T>      元素类型
     */
    public static <T> void forEachWithIndex(LinkedHashSet<T> set, BiConsumer<Integer, T> consumer) {
        int index = 0;
        for (T element : set) {
            consumer.accept(index++, element);
        }
    }
}
