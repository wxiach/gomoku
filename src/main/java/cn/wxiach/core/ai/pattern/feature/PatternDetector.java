package cn.wxiach.core.ai.pattern.feature;

import cn.wxiach.core.ai.pattern.Pattern;

public interface PatternDetector<T> {

    Pattern<T> detect(String line);
}
