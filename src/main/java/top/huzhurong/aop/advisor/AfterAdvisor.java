package top.huzhurong.aop.advisor;

import top.huzhurong.aop.invocation.CglibInvocation;
import top.huzhurong.aop.invocation.Invocation;
import top.huzhurong.aop.invocation.JdkInvocation;

/**
 * 后置增强
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
public class AfterAdvisor extends AbstractAdvisor implements MethodInterceptor {

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } finally {
            if (invocation instanceof CglibInvocation) {
                CglibInvocation cglibInvocation = (CglibInvocation) invocation;
                cglibInvocation.getMethod().setAccessible(true);
                cglibInvocation.getMethod().invoke(cglibInvocation.getTarget(), cglibInvocation.getArgs());
            } else if (invocation instanceof JdkInvocation) {
                JdkInvocation jdkInvocation = (JdkInvocation) invocation;
                jdkInvocation.getMethod().setAccessible(true);
                jdkInvocation.getMethod().invoke(jdkInvocation.getTarget(), jdkInvocation.getArgs());
            }
        }
    }
}
