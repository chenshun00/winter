package top.huzhurong.aop.other;

import lombok.extern.slf4j.Slf4j;
import top.huzhurong.aop.advisor.transaction.manager.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/1
 */
@Slf4j
public class TestDao {
    public Integer addTest(Test test) {
        try {
            Connection connection = ConnectionManager.get().getConnection();
            log.info("addTest方法当中，从ConnectionManager中获取数据库链接:{}", connection);
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into test(`id`,`name`,`age`) values (?,?,?)");
            preparedStatement.setInt(1, test.getId());
            preparedStatement.setString(2, test.getName());
            preparedStatement.setInt(3, test.getAge());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer updatetestById(Test test) {
        try {
            Connection connection = ConnectionManager.get().getConnection();
            log.info("updatetestById方法当中，从ConnectionManager中获取数据库链接:{}", connection);
            log.info("执行的sql:update `test` set name = {} , age = {} where id = {}", test.getName(), test.getAge(), test.getId());
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update `test` set name = ? , age = ? where id = ?");
            preparedStatement.setString(1, test.getName());
            preparedStatement.setInt(2, test.getAge());
            preparedStatement.setInt(3, test.getId());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Test getTestById(Integer id) {
        try {
            Connection connection = ConnectionManager.get().getConnection();
            log.info("getTestById方法当中，从ConnectionManager中获取数据库链接:{}", connection);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from test where id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            int fetchSize = resultSet.getFetchSize();
            if (fetchSize > 1) {
                throw new IllegalStateException("query one object , but found many object from database");
            }
            if (resultSet.next()) {
                int ids = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int age = resultSet.getInt(3);
                return new Test(ids, name, age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
