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
 * @author luobo.cs@raycloud.com
 * @since 2018/9/8
 */

public abstract class InitTest {

    protected Init init = new Init();
    protected BeanFactory beanFactory;

    @Before
    public void before() {
//        init.setBootClass(InitTest.class);
//        Set<ClassInfo> classInfoSet = init.scan("top.huzhurong.ioc.scan.test");
//        init.instantiation(classInfoSet);
//        beanFactory = init.getBeanFactory();
    }

    @Test
    public void scan() {
        Set<ClassInfo> classInfoSet = init.scan("top.huzhurong.ioc.scan.test");
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