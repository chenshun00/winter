package top.huzhurong.ioc;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Before;
import org.junit.Test;
import top.huzhurong.ioc.annotation.EnableConfiguration;
import top.huzhurong.ioc.bean.ClassInfo;
import top.huzhurong.ioc.scan.test.TestScan1;
import top.huzhurong.ioc.transaction.TestService;

import java.util.Set;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/13
 */
@EnableConfiguration
public class CglibProxyTest extends InitTest {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/test?useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true";
    private static final String user = "root";
    private static final String password = "chenshun";

    private DruidDataSource druidDataSource;

    @Before
    public void before() {
        init.setBootClass(CglibProxyTest.class);
        Set<ClassInfo> classInfoSet = init.scan("top.huzhurong.ioc");
        beanFactory = init.getBeanFactory();

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
        init.instantiation(classInfoSet);
    }

    @Test
    public void cglib() {
        TestScan1 testScan1 = (TestScan1) this.beanFactory.getBean("hhh");
        testScan1.hello(); //use cglib
    }

    @Test
    public void transactionTest() {
        TestService testService = (TestService) this.beanFactory.getBean("testService");
        top.huzhurong.ioc.transaction.Test test = top.huzhurong.ioc.transaction.Test.builder()
                .age(22).id(18).name("test").build();
        System.out.println("--dao");
        System.out.println(testService.getTestDao());
        System.out.println("dao--");
        testService.addTest(test);
    }
}
