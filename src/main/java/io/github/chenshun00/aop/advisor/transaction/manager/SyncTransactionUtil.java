package io.github.chenshun00.aop.advisor.transaction.manager;

import io.github.chenshun00.aop.advisor.transaction.definition.TransactionStatus;
import lombok.NonNull;

/**
 * 事务同步
 *
 * @author chenshun00@gmail.com
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
    private final static ThreadLocal<Boolean> currentTransactionActive = new NameThreadLocal<>("Current transaction iActive");

    static {
        currentTransactionActive.set(false);
    }

    public static boolean isSynchronizationActive() {
        if (currentTransactionActive.get() == null) {
            currentTransactionActive.set(false);
        }
        System.out.println(currentTransactionActive + "====" + currentTransactionActive.get());

        return (currentTransactionActive.get());
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

    public static void setStatus(@NonNull TransactionStatus status, @NonNull Integer level, @NonNull Boolean active, @NonNull Boolean readOnly) {
        statusThreadLocal.set(status);
        currentTransactionActive.set(active);
        currentTransactionIsolationLevel.set(level);
        currentTransactionReadOnly.set(readOnly);
    }

    public static TransactionStatus getTransactionStatus() {
        return statusThreadLocal.get();
    }

}
