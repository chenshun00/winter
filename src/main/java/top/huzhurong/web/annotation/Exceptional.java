package top.huzhurong.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Exceptional {

    Class<? extends Throwable>[] value();

}
