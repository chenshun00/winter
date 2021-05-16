package io.github.chenshun00.aop.advisor;

import io.github.chenshun00.aop.invocation.CglibInvocation;
import io.github.chenshun00.aop.invocation.Invocation;
import io.github.chenshun00.aop.invocation.JdkInvocation;
import io.github.chenshun00.aop.advisor.pointcut.Pointcut;

import java.lang.reflect.Method;

/**
 * 前置增强
 *
 * @author chenshun00@gmail.com
 * @since 2018/8/26
 */
public class BeforeAdvisor extends AbstractAdvisor implements MethodInterceptor {

    public BeforeAdvisor(Method advisorMethod, Object proxy, Object[] args, Pointcut pointcut) {
        super(advisorMethod, proxy, args, pointcut);
    }

    public Object invoke(Invocation invocation) throws Throwable {
        if (invocation instanceof CglibInvocation || invocation instanceof JdkInvocation) {
            Method method = getAdvisorMethod();
            method.setAccessible(true);
            Object[] args = getArgs();
            method.invoke(getProxy(), args);
        } else {
            throw new IllegalStateException("代理状态异常!");
        }
        return invocation.proceed();
    }

}
