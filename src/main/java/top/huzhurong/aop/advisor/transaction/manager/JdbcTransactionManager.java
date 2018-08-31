package top.huzhurong.aop.advisor.transaction.manager;

import top.huzhurong.aop.advisor.transaction.Transaction;
import top.huzhurong.aop.advisor.transaction.TransactionManager;
import top.huzhurong.aop.advisor.transaction.definition.DefaultTransactionStatus;
import top.huzhurong.aop.advisor.transaction.definition.TransactionDefinition;
import top.huzhurong.aop.advisor.transaction.definition.TransactionStatus;

/**
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
        //上下文没有事务
        Transaction transaction = new Transaction();
        TransactionStatus transactionStatus = new DefaultTransactionStatus(transaction, false, false);
        //事务绑定
        SyncTransactionUtil.setStatus(definition.getIsolationLevel(), true, false);
        return transactionStatus;
    }

    private TransactionStatus handleExistTransaction(TransactionDefinition definition) {
        Transaction transaction = new Transaction();
        return new DefaultTransactionStatus(transaction, false, false);
    }

    @Override
    public void commit(TransactionStatus status) {
    }

    @Override
    public void rollback(TransactionStatus status) {

    }
}
