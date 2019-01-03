package top.huzhurong.xbatis;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import top.huzhurong.ioc.bean.aware.FactoryBean;
import top.huzhurong.ioc.bean.aware.InitAware;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author chenshun00@gmail.com
 * @since 2018/10/6
 */
@Slf4j
public class MybatisFactoryBean implements FactoryBean<SqlSessionFactory>, InitAware {

    private SqlSessionFactory sqlSessionFactory;

    private String id = MybatisFactoryBean.class.getName();

    @Getter
    @Setter
    private DataSource dataSource;

    @Setter
    @Getter
    private String xbatisConfig;

    private SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

    @Override
    public SqlSessionFactory getObject() {
        return this.buildSqlSessionFactory();
    }

    private SqlSessionFactory buildSqlSessionFactory() {
        //1、搭建 xmlConfigBuilder

        if (this.xbatisConfig == null) {
            throw new RuntimeException("未设置mybatis-config的值，可在application.properties中设置");
        }

        InputStream resource = null;
        try {
            resource = Resources.getResourceAsStream(xbatisConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resource == null) {
            throw new RuntimeException(String.format("文件【%s】未找到", xbatisConfig));
        }


        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(resource, id, null);
        //2、获取Configuration
        Configuration configuration = xmlConfigBuilder.parse();
        //3、完善Configuration
        TransactionFactory transactionFactory = new ManagedTransactionFactory();
        Environment environment = new Environment(id, transactionFactory, this.dataSource);
        configuration.setEnvironment(environment);
        this.sqlSessionFactory = builder.build(configuration);
        return sqlSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return SqlSessionFactory.class;
    }

    @Override
    public void initBean() {
        log.info("初始化【MybatisFactoryBean】，获取mybatis SqlSessionFactory");
        if (this.sqlSessionFactory == null) {
            throw new RuntimeException("sqlSessionFactory 为null");
        }
    }
}
