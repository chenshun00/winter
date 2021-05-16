package io.github.chenshun00.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * inject a field
 *
 * @author chenshun00@gmail.com
 * @since 2018/9/8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Inject {
    /**
     * inject bean's name
     */
    String value() default "";
}
