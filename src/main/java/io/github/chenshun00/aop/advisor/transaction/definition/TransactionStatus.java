package io.github.chenshun00.aop.advisor.transaction.definition;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/29
 */
public interface TransactionStatus {

    boolean isNewTransaction();

    void setRollbackOnly();

    boolean isRollbackOnly();

    boolean isCompleted();
}
