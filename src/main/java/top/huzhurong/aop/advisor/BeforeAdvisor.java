package top.huzhurong.aop.advisor;

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

    public Object invoke(Invocation invocation) throws Throwable {
        if (invocation instanceof CglibInvocation) {
            CglibInvocation cglibInvocation = (CglibInvocation) invocation;
            Method method = cglibInvocation.getMethod();
            method.setAccessible(true);
            Object[] args = cglibInvocation.getArgs();
            method.invoke(cglibInvocation.getTarget(), args);
        }
        if (invocation instanceof JdkInvocation) {
            JdkInvocation jdkInvocation = (JdkInvocation) invocation;
            Method method = jdkInvocation.getMethod();
            method.setAccessible(true);
            Object[] args = jdkInvocation.getArgs();
            method.invoke(jdkInvocation.getTarget(), args);
        }
        return invocation.proceed();
    }

}
