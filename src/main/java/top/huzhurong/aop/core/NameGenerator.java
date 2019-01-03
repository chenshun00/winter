package top.huzhurong.aop.core;

import java.util.UUID;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/11
 */
public class NameGenerator {

    public static String generator(Object object) {
        return object.getClass().getSimpleName() + "#" + UUID.randomUUID().toString().split("-")[2];
    }
}
