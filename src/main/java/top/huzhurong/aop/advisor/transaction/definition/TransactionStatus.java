package top.huzhurong.aop.advisor.transaction.definition;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/29
 */
public interface TransactionStatus {

    boolean isNewTransaction();

    void setRollbackOnly();

    boolean isRollbackOnly();

    boolean isCompleted();
}
