package cn.wxiach.ai.support;

@FunctionalInterface
public interface CharSequenceConverter<T> {
    CharSequence toCharSequence(T pattern);
}
