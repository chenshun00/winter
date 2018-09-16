package top.huzhurong.ioc.scan.test.config;

import com.alibaba.druid.pool.DruidDataSource;
import top.huzhurong.ioc.InitTest;
import top.huzhurong.ioc.annotation.Bean;
import top.huzhurong.ioc.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/16
 */
@Configuration
public class ConfigurationTest {

    @Bean
    public Hello hello() {
        return new Hello();
    }


    @Bean
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setMaxActive(10);
        druidDataSource.setUrl(InitTest.url);
        if (InitTest.user.equals("root") && InitTest.password.equals("chenshun")) {
            System.err.println("出现错误请检查数据url,user,password是否替换，未出现错误请忽略该消息");
        }
        druidDataSource.setUsername(InitTest.user);
        druidDataSource.setPassword(InitTest.password);
        druidDataSource.setValidationQuery("select 'x'");
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
        return druidDataSource;
    }
}
