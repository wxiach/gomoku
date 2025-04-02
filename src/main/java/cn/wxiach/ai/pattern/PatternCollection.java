package cn.wxiach.ai.pattern;

import java.util.Collection;

public interface PatternCollection<T> {

    Pattern<T> empty();

    Pattern<T> win();

    Collection<Pattern<T>> patterns();
}
