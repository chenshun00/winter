package top.huzhurong.aop.advisor.transaction.manager;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.huzhurong.aop.advisor.transaction.Transaction;
import top.huzhurong.aop.advisor.transaction.TransactionManager;
import top.huzhurong.aop.advisor.transaction.definition.DefaultTransactionStatus;
import top.huzhurong.aop.advisor.transaction.definition.TransactionDefinition;
import top.huzhurong.aop.advisor.transaction.definition.TransactionStatus;
import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.bean.aware.InitAware;
import top.huzhurong.ioc.bean.aware.IocContainerAware;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 事务管理，暂时不考虑传播行为，后期会加入
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/8/31
 */
@Slf4j
public class JdbcTransactionManager implements TransactionManager, IocContainerAware, InitAware {

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws SQLException {

        Transaction transaction = doGetTransaction();
        //当前上下文是否有事务
        if (SyncTransactionUtil.isSynchronizationActive()) {
            //处理存在的事务
            return handleExistTransaction(definition, transaction);
        }
        //上下文没有事务
        TransactionStatus transactionStatus = new DefaultTransactionStatus(transaction, false, false);
        //事务绑定
        begin(transaction, definition);
        bindToThread(transactionStatus, definition);
        return transactionStatus;
    }

    private Transaction doGetTransaction() throws SQLException {
        TransactionStatus transactionStatus = SyncTransactionUtil.getTransactionStatus();
        if (transactionStatus != null) {
            DefaultTransactionStatus status = (DefaultTransactionStatus) transactionStatus;
            Transaction transaction = (Transaction) status.getTransaction();
            transaction.setNewTransaction(false);
            transaction.setActive(true);
            return transaction;
        } else {
            Transaction transaction = new Transaction();
            transaction.setActive(true);
            transaction.setNewTransaction(true);
            ConnectionManager connectionManager = new ConnectionManager();
            Connection connection = this.dataSource.getConnection();
            connectionManager.setConnection(connection);
            connectionManager.setNewConnection(true);
            ConnectionManager.setConnectionThreadLocal(connectionManager);
            transaction.setConnectionManager(connectionManager);
            return transaction;
        }
    }

    private void bindToThread(TransactionStatus transactionStatus, TransactionDefinition definition) {
        SyncTransactionUtil.setStatus(transactionStatus, definition.getIsolationLevel(), true, definition.isReadOnly());
    }

    /**
     * @param transaction 当前事务
     * @param definition  定义好的事务数据
     */
    private void begin(Transaction transaction, TransactionDefinition definition) {
        try {
            Connection connection = transaction.getConnection().getConnection();
            if (definition.isReadOnly()) {
                connection.setReadOnly(true);
                try (Statement statement = connection.createStatement()) {
                    statement.execute("set transaction read only");
                }
            }

            connection.setAutoCommit(false);
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

    }

    private TransactionStatus handleExistTransaction(TransactionDefinition definition, Transaction transaction) {
        begin(transaction, definition);
        //暂时不考虑事务的传播行为，即一致当成required处理
        return SyncTransactionUtil.getTransactionStatus();
    }

    @Override
    public void commit(TransactionStatus status) throws SQLException {
        if (status.isCompleted()) {
            throw new IllegalStateException("the transaction has bean commit");
        }
        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
        if (defStatus.isLocalRollbackOnly()) {
            processRollback(defStatus);
            return;
        }
        processCommit(defStatus);
    }

    private void processCommit(DefaultTransactionStatus defStatus) {
        Transaction transaction = (Transaction) defStatus.getTransaction();
        Connection connection = transaction.getConnection().getConnection();
        try {
            log.info("事务提交");
            connection.commit();
            defStatus.setCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void processRollback(DefaultTransactionStatus defStatus) throws SQLException {
        Transaction transaction = (Transaction) defStatus.getTransaction();
        Connection connection = transaction.getConnection().getConnection();
        defStatus.setCompleted();
        log.info("事务回滚");
        connection.rollback();
    }

    @Override
    public void rollback(TransactionStatus status) throws SQLException {
        if (status.isCompleted()) {
            throw new IllegalStateException("the transaction has bean commit");
        }
        processRollback((DefaultTransactionStatus) status);
    }

    @Getter
    @Setter
    private DataSource dataSource;
    private IocContainer iocContainer;

    @Override
    public void setIocContainer(IocContainer iocContainer) {
        this.iocContainer = iocContainer;
    }

    @Override
    public void initBean() {
        if (iocContainer == null) {
            throw new IllegalStateException("inject iocContainer failed,this is a bug! ^v^ ");
        }
        log.info("inject dataSource to jdbcTransactionManager");
        this.dataSource = (DataSource) iocContainer.getBean("datasource");
    }
}
