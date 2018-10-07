package top.huzhurong.util;

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

    public static String handleClassName(Class<?> aClass) {
        return handleClassName(aClass.getSimpleName());
    }

    public static String handleClassName(String simpleName) {
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
    }

    public static boolean containSpace(String values) {
        if (values == null || values.trim().length() == 0) {
            return true;
        }
        return values.contains(" ");
    }

}
