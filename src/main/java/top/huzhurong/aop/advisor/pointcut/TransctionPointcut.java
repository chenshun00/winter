package top.huzhurong.aop.advisor.pointcut;

import top.huzhurong.aop.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/3
 */
public class TransctionPointcut implements Pointcut {
    @Override
    public boolean match(Method method) {
        return method.getDeclaredAnnotation(Transactional.class) != null;
    }
}
