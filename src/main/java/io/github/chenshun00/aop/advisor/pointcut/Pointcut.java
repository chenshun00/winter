package io.github.chenshun00.aop.advisor.pointcut;

import java.lang.reflect.Method;

/**
 * 切点匹配
 *
 * @author chenshun00@gmail.com
 * @since 2018/9/3
 */
public interface Pointcut {

    /**
     * 匹配给定的方法
     *
     * @param method 方法
     * @return true 匹配成功，false 匹配失败
     */
    boolean match(Method method);

}
