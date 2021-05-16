package io.github.chenshun00.ioc;

import io.github.chenshun00.ioc.annotation.EnableConfiguration;
import io.github.chenshun00.ioc.scan.test.TestScan1;
import io.github.chenshun00.ioc.transaction.TestService;
import org.junit.Before;
import org.junit.Test;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/13
 */
@EnableConfiguration
public class CglibProxyTest extends InitTest {

    @Before
    public void before() {
        winter.setBootClass(CglibProxyTest.class);
        winter.start();
        this.beanFactory = winter.getIocContainer();
    }

    @Test
    public void cglib() {
        TestScan1 testScan1 = (TestScan1) this.beanFactory.getBean("hhh");
        testScan1.hello(); //use cglib
    }

    @Test
    public void transactionTest() throws ClassNotFoundException, InterruptedException {
        TestService testService = (TestService) this.beanFactory.getBean("testService");
        io.github.chenshun00.ioc.transaction.Test test = io.github.chenshun00.ioc.transaction.Test.builder()
                .age(22).id(18).name("test").build();
        long l = System.currentTimeMillis();
        testService.addTest(test);
        System.out.println(System.currentTimeMillis() - l);


        Thread.sleep(1000000L);
    }
}
