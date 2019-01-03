package top.huzhurong.aop.advisor.transaction.definition;

import java.sql.Connection;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/29
 */
public interface TransactionDefinition {

    int PROPAGATION_REQUIRED = 0;

    int ISOLATION_READ_UNCOMMITTED = Connection.TRANSACTION_REPEATABLE_READ;


    int TIMEOUT_DEFAULT = -1;

    int getPropagationBehavior();

    int getIsolationLevel();

    int getTimeout();

    boolean isReadOnly();

    default String getName() {
        return "transaction-name";
    }

}
