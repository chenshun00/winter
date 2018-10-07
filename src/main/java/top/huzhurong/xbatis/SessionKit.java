package top.huzhurong.xbatis;

import lombok.NonNull;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import top.huzhurong.aop.advisor.transaction.manager.ConnectionManager;
import top.huzhurong.ioc.annotation.Inject;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author luobo.cs@raycloud.com
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
