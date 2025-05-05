package cn.wxiach.utils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.BiConsumer;

public class CollectionUtils {

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !collection.isEmpty();
    }


    /**
     * Iterate through a LinkedHashSet and apply a consumer to each element with its index.
     *
     * @param set
     * @param consumer
     * @param <T>
     */
    public static <T> void forEachWithIndex(LinkedHashSet<T> set, BiConsumer<Integer, T> consumer) {
        int index = 0;
        for (T element : set) {
            consumer.accept(index++, element);
        }
    }
}
