package top.huzhurong.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * inject a field
 *
 * @author luobo.cs@raycloud.com
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
