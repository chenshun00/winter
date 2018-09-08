package top.huzhurong.aop.core;

import java.util.Arrays;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/3
 */
public class StringUtil {
    public static void validate(String... param) {
        Arrays.stream(param).forEach(str -> {
            if (str == null || str.length() == 0) {
                throw new IllegalArgumentException("参数状态异常:" + Arrays.toString(param));
            }
        });
    }
}
