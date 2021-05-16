package io.github.chenshun00.web.util;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.chenshun00.ioc.InitTest;
import io.github.chenshun00.ioc.annotation.Bean;
import io.github.chenshun00.ioc.annotation.Configuration;
import io.github.chenshun00.ioc.bean.aware.Environment;
import io.github.chenshun00.ioc.bean.aware.EnvironmentAware;

import javax.sql.DataSource;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/16
 */
@Configuration
public class ConfigurationTest implements EnvironmentAware {

    private Environment environment;

    @Bean
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setMaxActive(10);
        druidDataSource.setInitialSize(5);
        if (InitTest.user.equals("root") && InitTest.password.equals("chenshun")) {
            System.err.println("出现错误请检查数据url,user,password是否替换，未出现错误请忽略该消息");
        }
        druidDataSource.setUrl(environment.getString("url"));
        druidDataSource.setUsername(environment.getString("user"));
        druidDataSource.setPassword(environment.getString("password"));
        druidDataSource.setValidationQuery("select 'x'");
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
        return druidDataSource;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
