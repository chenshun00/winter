package top.huzhurong.ioc;

import org.junit.Before;
import org.junit.Test;
import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.scan.test.config.Hello;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/16
 */
public class ConfigTest {

    private Winter winter;

    @Before
    public void before() {
        winter = new Winter(ConfigTest.class);
    }

    @Test
    public void testConfigurationAnnotation() throws SQLException {
        IocContainer iocContainer = winter.getIocContainer();
        Hello bean = iocContainer.getBean(Hello.class);
        System.out.println(bean.hi());
        DataSource dataSource = iocContainer.getBean(DataSource.class);
        System.out.println(dataSource);
        System.out.println(dataSource.getConnection());
    }

}
