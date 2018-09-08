package top.huzhurong.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * bean , annotation in Service,Dao
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/9/8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Bean {
    String value() default "";
}
