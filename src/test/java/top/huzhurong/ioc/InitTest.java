package top.huzhurong.ioc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.huzhurong.ioc.bean.BeanFactory;
import top.huzhurong.ioc.bean.ClassInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/8
 */

public class InitTest {

    public static final String url = "jdbc:mysql://127.0.0.1:3306/test?useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true";
    public static final String user = "root";
    public static final String password = "chenshun";

    protected Winter winter = new Winter();
    protected BeanFactory beanFactory;

    @Before
    public void before() {

    }

    @Test
    public void scan() {
        Set<ClassInfo> classInfoSet = winter.scan("top.huzhurong.ioc.scan.test");
        classInfoSet.forEach(set -> {
            System.out.println(set.getaClass());
            System.out.println(set.getClassName());
        });
    }

    @Test
    public void className() {
        String simpleName = InitTest.class.getSimpleName();
        Assert.assertEquals("InitTest", simpleName);
    }

    @Test
    public void filter() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4);
        integers.stream().filter(integer -> integer >= 2).forEach(System.out::println);
    }
}