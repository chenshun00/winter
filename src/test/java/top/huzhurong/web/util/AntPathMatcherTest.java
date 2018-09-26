package top.huzhurong.web.util;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/26
 */
public class AntPathMatcherTest {

    private PathMatcher pathMatcher = new AntPathMatcher();

    private String info = "/user/{name}/{age}/bb";
    private String info2 = "/user/cc/22/bb";

    private String pattern = "/user/{*}/{*}/**";

    @Test
    public void isPattern() {
        assertFalse(pathMatcher.isPattern(info));
        assertTrue(pathMatcher.isPattern(pattern));
    }

    @Test
    public void match() {
        boolean match = pathMatcher.match(info, info2);
        assertTrue(match);
    }

    @Test
    public void matchStart() {
        boolean b = pathMatcher.matchStart(pattern, info);
        assertTrue(b);
    }

    @Test
    public void extractPathWithinPattern() {
        String s = pathMatcher.extractPathWithinPattern(info, info2);
        System.out.println(s);
    }

    @Test
    public void extractUriTemplateVariables() {
        Map<String, String> map = pathMatcher.extractUriTemplateVariables(info, info2);
        System.out.println(JSONObject.toJSONString(map));
    }

    @Test
    public void combine() {
    }

    @Test
    public void getPatternComparator() {
    }
}