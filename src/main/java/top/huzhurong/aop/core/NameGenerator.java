package top.huzhurong.aop.core;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/11
 */
public class NameGenerator {

    private static AtomicLong count = new AtomicLong(1);

    public static String generator(Object object) {
        return object.getClass().getSimpleName() + "#" + count.getAndIncrement();
    }
}
