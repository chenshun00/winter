package top.huzhurong.aop.advisor.transaction.manager;

import top.huzhurong.aop.advisor.transaction.definition.TransactionStatus;

/**
 * 事务同步
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/8/29
 */
public class SyncTransactionUtil {

    /**
     * 事务状态
     */
    private static final ThreadLocal<TransactionStatus> statusThreadLocal = new NameThreadLocal<>("Current transaction status");

    /**
     * 当前事务名称，log和记录用，默认是class+method
     */
    private static final ThreadLocal<String> currentTransactionName = new NameThreadLocal<>("Current transaction name");

    /**
     * 只读事务
     */
    private static final ThreadLocal<Boolean> currentTransactionReadOnly = new NameThreadLocal<>("Current transaction read-only status");
    /**
     * 隔离级别
     */
    private static final ThreadLocal<Integer> currentTransactionIsolationLevel = new NameThreadLocal<>("Current transaction isolation level");
    /**
     * 事务是否激活
     */
    private static final ThreadLocal<Boolean> currentTransactionActive = new NameThreadLocal<>("Current transaction iActive");

    public static boolean isSynchronizationActive() {
        return (currentTransactionActive.get() != null);
    }

    public static boolean isReadOnlyTransaction() {
        return currentTransactionReadOnly.get() != null;
    }


    public static void clear() {
        statusThreadLocal.remove();
        currentTransactionName.remove();
        currentTransactionReadOnly.set(false);
        currentTransactionIsolationLevel.set(0);
        currentTransactionActive.set(false);
    }

    public static void setStatus(TransactionStatus status, Integer level, boolean active, boolean readOnly) {
        statusThreadLocal.set(status);
        currentTransactionActive.set(active);
        currentTransactionIsolationLevel.set(level);
        currentTransactionReadOnly.set(readOnly);
    }

    public static TransactionStatus getTransactionStatus() {
        return statusThreadLocal.get();
    }

}
