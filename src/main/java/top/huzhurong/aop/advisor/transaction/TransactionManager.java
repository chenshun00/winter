package top.huzhurong.aop.advisor.transaction;

import top.huzhurong.aop.advisor.transaction.definition.TransactionDefinition;
import top.huzhurong.aop.advisor.transaction.definition.TransactionStatus;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/29
 */
public interface TransactionManager {

    TransactionStatus getTransaction(TransactionDefinition definition);

    void commit(TransactionStatus status);

    void rollback(TransactionStatus status);
}
