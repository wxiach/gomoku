package cn.wxiach.ai.pattern;

import java.util.Collection;

public interface PatternDetector<T> {
    Collection<Pattern<T>> detect(Collection<T> matchLines);

    Pattern<T> detect(T matchLine);
}
