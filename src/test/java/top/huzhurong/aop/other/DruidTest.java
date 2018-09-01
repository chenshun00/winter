package top.huzhurong.aop.other;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/1
 */
@Slf4j
public class DruidTest {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/test?useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true";
    private static final String user = "root";
    private static final String password = "chenshun";


    @Test
    public void testDruidUsage() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setMaxActive(10);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(user);
        druidDataSource.setPassword(password);
        druidDataSource.setValidationQuery("select 'x'");
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);

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
}
