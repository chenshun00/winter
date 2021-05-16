package io.github.chenshun00.aop.advisor.pointcut;

import io.github.chenshun00.aop.annotation.Transactional;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/3
 */
public class TransactionPointcut implements Pointcut {
    @Override
    public boolean match(Method method) {
        return method.getDeclaredAnnotation(Transactional.class) != null && method.getModifiers() == Modifier.PUBLIC;
    }
}
