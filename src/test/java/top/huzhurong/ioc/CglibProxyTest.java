package top.huzhurong.ioc;

import org.junit.Before;
import org.junit.Test;
import top.huzhurong.ioc.annotation.EnableConfiguration;
import top.huzhurong.ioc.bean.ClassInfo;
import top.huzhurong.ioc.scan.test.TestScan1;

import java.util.Set;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/13
 */
@EnableConfiguration
public class CglibProxyTest extends InitTest {

    @Before
    public void before() {
        init.setBootClass(CglibProxyTest.class);
        Set<ClassInfo> classInfoSet = init.scan("top.huzhurong.ioc.scan.test");
        init.instantiation(classInfoSet);
        beanFactory = init.getBeanFactory();
    }

    @Test
    public void cglib() {
        TestScan1 testScan1 = (TestScan1) this.beanFactory.getBean("hhh");
        testScan1.hello(); //use cglib
    }
}
