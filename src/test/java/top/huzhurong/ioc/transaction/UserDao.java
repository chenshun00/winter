package top.huzhurong.ioc.transaction;

import lombok.extern.slf4j.Slf4j;
import top.huzhurong.aop.advisor.transaction.manager.ConnectionManager;
import top.huzhurong.ioc.annotation.Bean;

import java.sql.*;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/18
 */
@Slf4j
@Bean
public class UserDao {
    public Integer addUser(User user) {
        try {
            Connection connection = ConnectionManager.get().getConnection();
            log.info("addUser方法当中，从ConnectionManager中获取数据库链接:{}", connection);
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into user(`id`,`name`,`birthday`) values (?,?,?)");
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setDate(3, new Date(user.getBirthday().getTime()));
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer updateUserById(User user) {
        try {
            Connection connection = ConnectionManager.get().getConnection();
            log.info("updatetestById方法当中，从ConnectionManager中获取数据库链接:{}", connection);
            log.info("执行的sql:update `user` set name = {} , birthday = {} where id = {}", user.getName(), user.getBirthday(), user.getId());
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update `user` set name =  ? , birthday = ? where id = ?");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setDate(2, new Date(user.getBirthday().getTime()));
            preparedStatement.setInt(3, user.getId());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getTestById(Integer id) {
        try {
            Connection connection = ConnectionManager.get().getConnection();
            log.info("getTestById方法当中，从ConnectionManager中获取数据库链接:{}", connection);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from user where id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            int fetchSize = resultSet.getFetchSize();
            if (fetchSize > 1) {
                throw new IllegalStateException("query one object , but found many object from database");
            }
            if (resultSet.next()) {
                int ids = resultSet.getInt(1);
                String name = resultSet.getString(2);
                java.util.Date birthday = resultSet.getDate(3);
                return new User(ids, name, birthday);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
