package top.huzhurong.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 前置
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Advisor
public @interface Before {
    String value();
}
