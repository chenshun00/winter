package top.huzhurong.ioc;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Before;
import org.junit.Test;
import top.huzhurong.ioc.annotation.EnableConfiguration;
import top.huzhurong.ioc.scan.test.AAA;
import top.huzhurong.ioc.transaction.TestService;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/13
 */
@EnableConfiguration(proxyByClass = false)
public class JdkProxyTest extends InitTest {

    private DruidDataSource druidDataSource;

    @Before
    public void before() {
        init.setBootClass(CglibProxyTest.class);
        beanFactory = init.getIocContainer();

        druidDataSource = new DruidDataSource();
        druidDataSource.setMaxActive(10);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(user);
        druidDataSource.setPassword(password);
        druidDataSource.setValidationQuery("select 'x'");
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);

        beanFactory.put("datasource", druidDataSource);
        init.instantiation();
    }


    @Test
    public void jdkProxy() {
        AAA testScan1 = (AAA) this.beanFactory.getBean("hhh");
        testScan1.gg();
    }


    @Test
    public void transactionTest() throws ClassNotFoundException {
        TestService testService = (TestService) this.beanFactory.getBean("testService");
        top.huzhurong.ioc.transaction.Test test = top.huzhurong.ioc.transaction.Test.builder()
                .age(22).id(18).name("test").build();
        System.out.println("--dao");
        System.out.println(testService.getTestDao());
        System.out.println("dao--");
        testService.addTest(test);
    }

}
