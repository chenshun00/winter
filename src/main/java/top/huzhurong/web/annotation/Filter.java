package top.huzhurong.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Filter {

    String value() default "";

    /**
     * 越高优先级越低
     */
    int order() default Integer.MAX_VALUE;
}
