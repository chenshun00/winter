package io.github.chenshun00.aop.advisor;

import io.github.chenshun00.aop.invocation.Invocation;
import io.github.chenshun00.aop.advisor.pointcut.PointcutAdvisor;

/**
 * 调用增强器方法
 *
 * @author chenshun00@gmail.com
 * @since 2018/8/26
 */
public interface MethodInterceptor extends PointcutAdvisor {

    Object invoke(Invocation invocation) throws Throwable;

}
