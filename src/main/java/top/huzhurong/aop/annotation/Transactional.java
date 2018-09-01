package top.huzhurong.aop.annotation;

import top.huzhurong.aop.advisor.transaction.definition.TransactionDefinition;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/1
 */
public @interface Transactional {
    String name() default "";

    int isolation() default TransactionDefinition.ISOLATION_READ_UNCOMMITTED;

    int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

    boolean readOnly() default false;

    int propagation()  default TransactionDefinition.PROPAGATION_REQUIRED;
}
