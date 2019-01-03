package top.huzhurong.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * bean , annotation in Service,Dao
 *
 * @author chenshun00@gmail.com
 * @since 2018/9/8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Bean {
    String value() default "";
}
