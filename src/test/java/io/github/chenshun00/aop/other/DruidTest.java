package io.github.chenshun00.aop.other;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.chenshun00.aop.advisor.transaction.manager.ConnectionManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/1
 */
@Slf4j
public class DruidTest {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/test?useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true";
    private static final String user = "root";
    private static final String password = "chenshun";

    private DruidDataSource druidDataSource;
    private TestDao testDao;

    @Before
    public void before() {
        druidDataSource = new DruidDataSource();
        druidDataSource.setMaxActive(10);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(user);
        druidDataSource.setPassword(password);
        druidDataSource.setValidationQuery("select 'x'");
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
    }

    @Test
    public void testDruidUsage() throws SQLException {
        Connection connection = druidDataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from test where id = ?");
        //first parameter is 1, the second tis 2
        preparedStatement.setInt(1, 1);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int age = resultSet.getInt(3);
            log.info("id:{},name:{},age:{}", id, name, age);
        }
    }

    @Test
    public void testTestDao() throws SQLException {
        ConnectionManager connectionManager = new ConnectionManager();
        try {
            testDao = new TestDao();
            connectionManager.setNewConnection(true);
            Connection connection = druidDataSource.getConnection();
            connection.setAutoCommit(false);
            connectionManager.setConnection(connection);
            ConnectionManager.setConnectionThreadLocal(connectionManager);

            io.github.chenshun00.aop.other.Test tt = io.github.chenshun00.aop.other.Test.builder().age(100).id(2).name("test").build();
            Integer test = testDao.addTest(tt);
            Assert.assertEquals(1, (int) test);
            io.github.chenshun00.aop.other.Test testById = testDao.getTestById(tt.getId());
            log.debug("第一次查询:{}", testById);
            Assert.assertNotNull(testById);
            Assert.assertEquals(tt.getName(), testById.getName());

            tt.setName("test3");
            Integer integer = testDao.updatetestById(tt);
            Assert.assertEquals(1, (int) integer);
            testById = testDao.getTestById(tt.getId());
            log.debug("第二次查询:{}", testById);
        } catch (SQLException sql) {
            connectionManager.getConnection().rollback();
            log.info("事务回滚");
            sql.printStackTrace();
            System.exit(2);
        }
        connectionManager.getConnection().commit();
        log.info("事务提交");
    }
}
