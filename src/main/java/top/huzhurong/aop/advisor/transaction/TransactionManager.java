package top.huzhurong.aop.advisor.transaction;

import top.huzhurong.aop.advisor.transaction.definition.TransactionDefinition;
import top.huzhurong.aop.advisor.transaction.definition.TransactionStatus;

import java.sql.SQLException;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/29
 */
public interface TransactionManager {

    TransactionStatus getTransaction(TransactionDefinition definition) throws SQLException;

    void commit(TransactionStatus status) throws SQLException;

    void rollback(TransactionStatus status) throws SQLException;
}
