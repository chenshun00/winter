package io.github.chenshun00.xbatis;

import io.github.chenshun00.ioc.annotation.Inject;
import lombok.NonNull;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import io.github.chenshun00.aop.advisor.transaction.manager.ConnectionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author chenshun00@gmail.com
 * @since 2018/10/7
 */
public class SessionKit {

    @Inject
    private DataSource dataSource;

    public SqlSession getSqlSession(@NonNull SqlSessionFactory sqlSessionFactory, ExecutorType executorType) throws SQLException {
        if (executorType == null) {
            executorType = ExecutorType.SIMPLE;
        }
        ConnectionManager connectionManager;
        if (ConnectionManager.exist()) {
            connectionManager = ConnectionManager.get();
            connectionManager.setNewConnection(false);
        } else {
            connectionManager = new ConnectionManager();
            connectionManager.setConnection(dataSource.getConnection());
            connectionManager.setNewConnection(true);
        }
        return sqlSessionFactory.openSession(executorType, connectionManager.getConnection());
    }
}
