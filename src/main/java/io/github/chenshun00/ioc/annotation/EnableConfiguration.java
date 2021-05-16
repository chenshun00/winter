package io.github.chenshun00.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EnableConfiguration {
    String[] value() default {"aop", "transaction"};

    /**
     * use cglib as default model
     */
    boolean proxyByClass() default true;

}
