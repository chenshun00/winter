package top.huzhurong.aop.advisor;

import top.huzhurong.aop.advisor.pointcut.Pointcut;
import top.huzhurong.aop.invocation.CglibInvocation;
import top.huzhurong.aop.invocation.Invocation;
import top.huzhurong.aop.invocation.JdkInvocation;

import java.lang.reflect.Method;

/**
 * 前置增强
 *
 * @author luobo.cs@raycloud.com
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
