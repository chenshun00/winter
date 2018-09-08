package top.huzhurong.ioc;

import org.junit.Assert;
import org.junit.Test;
import top.huzhurong.ioc.bean.BeanFactory;
import top.huzhurong.ioc.bean.ClassInfo;
import top.huzhurong.ioc.scan.test.TestScan1;

import java.util.Set;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/8
 */
public class InitTest {

    private Init init = new Init();

    @Test
    public void scan() {
        Set<ClassInfo> classInfoSet = init.scan("top.huzhurong.ioc.scan.test");
        classInfoSet.forEach(set -> {
            System.out.println(set.getaClass());
            System.out.println(set.getClassName());
        });
    }

    @Test
    public void instantiation() {
        Set<ClassInfo> classInfoSet = init.scan("top.huzhurong.ioc.scan.test");
        init.instantiation(classInfoSet);
        BeanFactory beanFactory = init.getBeanFactory();
        TestScan1 testScan1 = (TestScan1) beanFactory.getBean("hhh");
        System.out.println(testScan1);
        testScan1.hi();
        testScan1.hello();
    }

    @Test
    public void className() {
        String simpleName = InitTest.class.getSimpleName();
        Assert.assertEquals("InitTest", simpleName);
    }
}