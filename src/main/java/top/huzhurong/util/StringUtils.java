package top.huzhurong.util;

import java.util.*;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/3
 */
public class StringUtils {
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

    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static String[] tokenizeLocaleSource(String localeSource) {
        return tokenizeToStringArray(localeSource, "_ ", false, false);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return new String[0];
        }

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    public static String[] toStringArray(Collection<String> collection) {
        return collection.toArray(new String[0]);
    }
}
