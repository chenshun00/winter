package top.huzhurong.ioc;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Before;
import org.junit.Test;
import top.huzhurong.ioc.annotation.EnableConfiguration;
import top.huzhurong.ioc.transaction.TestService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/13
 */
@EnableConfiguration(proxyByClass = false)
public class JdkProxyTest extends InitTest {

    private DruidDataSource druidDataSource;

    @Before
    public void before() {
        winter.setBootClass(CglibProxyTest.class);
        beanFactory = winter.getIocContainer();

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
        winter.start();
    }


    @Test
    public void jdkProxy() throws SQLException {
//        AAA testScan1 = (AAA) this.beanFactory.getBean("hhh");
//        testScan1.gg();
        DataSource datasource = (DataSource) this.beanFactory.getBean("datasource");
        Connection connection = datasource.getConnection();
        System.out.println(connection);
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
