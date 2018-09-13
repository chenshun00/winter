package top.huzhurong.ioc;

import org.junit.Before;
import org.junit.Test;
import top.huzhurong.ioc.annotation.EnableConfiguration;
import top.huzhurong.ioc.bean.ClassInfo;
import top.huzhurong.ioc.scan.test.AAA;

import java.util.Set;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/13
 */
@EnableConfiguration(proxyByClass = false)
public class JdkProxyTest extends InitTest {

    @Before
    public void before() {
        init.setBootClass(JdkProxyTest.class);
        Set<ClassInfo> classInfoSet = init.scan("top.huzhurong.ioc.scan.test");
        init.instantiation(classInfoSet);
        beanFactory = init.getBeanFactory();
    }

    @Test
    public void jdkProxy() {
        AAA testScan1 = (AAA) this.beanFactory.getBean("hhh");
        testScan1.gg();
    }

}
