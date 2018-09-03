package top.huzhurong.aop.annotation;

import java.lang.annotation.*;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Inherited
public @interface Advisor {
}
