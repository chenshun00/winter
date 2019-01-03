package top.huzhurong.aop.annotation;

import top.huzhurong.aop.advisor.transaction.definition.TransactionDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional {
    String name() default "";

    int isolation() default TransactionDefinition.ISOLATION_READ_UNCOMMITTED;

    int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

    boolean readOnly() default false;

    int propagation() default TransactionDefinition.PROPAGATION_REQUIRED;
}
