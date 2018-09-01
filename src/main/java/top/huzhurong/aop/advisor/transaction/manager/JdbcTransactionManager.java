package top.huzhurong.aop.advisor.transaction.manager;

import top.huzhurong.aop.advisor.transaction.Transaction;
import top.huzhurong.aop.advisor.transaction.TransactionManager;
import top.huzhurong.aop.advisor.transaction.definition.DefaultTransactionStatus;
import top.huzhurong.aop.advisor.transaction.definition.TransactionDefinition;
import top.huzhurong.aop.advisor.transaction.definition.TransactionStatus;

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
public class JdbcTransactionManager implements TransactionManager {

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) {
        //当前上下文是否有事务
        if (SyncTransactionUtil.isSynchronizationActive()) {
            //处理存在的事务
            return handleExistTransaction(definition);
        }
        Transaction newTransaction = getNewTransaction();
        //上下文没有事务
        TransactionStatus transactionStatus = new DefaultTransactionStatus(newTransaction, false, false);
        //事务绑定
        begin(newTransaction, definition);
        bindToThread(transactionStatus, definition);
        return transactionStatus;
    }

    private void bindToThread(TransactionStatus transactionStatus, TransactionDefinition definition) {
        SyncTransactionUtil.setStatus(transactionStatus, definition.getIsolationLevel(), true, definition.isReadOnly());
    }

    private void begin(Transaction transaction, TransactionDefinition definition) {
        try {

            ConnectionManager connectionManager = new ConnectionManager();
            Connection connection = this.dataSource.getConnection();
            connectionManager.setConnection(connection);
            connectionManager.setNewConnection(true);
            ConnectionManager.setConnectionThreadLocal(connectionManager);
            transaction.setConnection(connectionManager);
            if (definition.isReadOnly()) {
                connection.setReadOnly(true);
                Statement statement = connection.createStatement();
                statement.execute("set transaction read only");
                statement.close();
            }

            connection.setAutoCommit(false);
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

    }

    /**
     * 获取一个新的 Transaction 标记，本身没有做什么用，只是用于标记当前事务激活
     */
    private Transaction getNewTransaction() {
        Transaction transaction = new Transaction();
        transaction.setActive(true);
        transaction.setNewTransaction(true);
        return transaction;
    }

    private TransactionStatus handleExistTransaction(TransactionDefinition definition) {

        Transaction transaction = new Transaction();
        return new DefaultTransactionStatus(transaction, false, false);
    }

    @Override
    public void commit(TransactionStatus status) {
        if (status.isCompleted()) {
            throw new IllegalStateException("the transaction has bean commit");
        }
        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
        if (defStatus.isLocalRollbackOnly()) {
            processRollback(defStatus, false);
            return;
        }
        processCommit(defStatus);
    }

    private void processCommit(DefaultTransactionStatus defStatus) {
        Transaction transaction = (Transaction) defStatus.getTransaction();
        Connection connection = transaction.getConnection().getConnection();
        try {
            connection.commit();
            defStatus.setCompleted();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void processRollback(DefaultTransactionStatus defStatus, boolean b) {
        Transaction transaction = (Transaction) defStatus.getTransaction();
        Connection connection = transaction.getConnection().getConnection();
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollback(TransactionStatus status) {
        if (status.isCompleted()) {
            throw new IllegalStateException("the transaction has bean commit");
        }
        processRollback((DefaultTransactionStatus) status, false);
    }

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
