package cn.wxiach.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author wxiach 2025/4/30
 */
public class Log {

    // 用于获取堆栈跟踪信息，保留类引用
    private static final StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    private static Logger getLogger() {
        Optional<Class<?>> caller = walker.walk(stackFrameStream -> stackFrameStream
                .filter(stackFrame -> stackFrame.getClassName().equals(Log.class.getName()))
                .<Class<?>>map(StackWalker.StackFrame::getDeclaringClass)
                .findFirst()
        );

        return LoggerFactory.getLogger(caller.orElse(Log.class));
    }

    public static void debug(String format, Object... args) {
        getLogger().debug(format, args);
    }

    public static void info(String format, Object... args) {
        getLogger().info(format, args);
    }

    public static void warn(String format, Object... args) {
        getLogger().warn(format, args);
    }

    public static void error(String format, Object... args) {
        getLogger().error(format, args);
    }
}
