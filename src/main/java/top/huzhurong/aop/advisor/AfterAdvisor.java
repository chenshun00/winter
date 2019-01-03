package top.huzhurong.aop.advisor;

import top.huzhurong.aop.advisor.pointcut.Pointcut;
import top.huzhurong.aop.invocation.CglibInvocation;
import top.huzhurong.aop.invocation.Invocation;
import top.huzhurong.aop.invocation.JdkInvocation;

import java.lang.reflect.Method;

/**
 * 后置增强
 *
 * @author chenshun00@gmail.com
 * @since 2018/8/26
 */
public class AfterAdvisor extends AbstractAdvisor implements MethodInterceptor {

    public AfterAdvisor(Method advisorMethod, Object proxy, Object[] args, Pointcut pointcut) {
        super(advisorMethod, proxy, args, pointcut);
    }

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } finally {
            if (invocation instanceof CglibInvocation || invocation instanceof JdkInvocation) {
                Method advisorMethod = getAdvisorMethod();
                advisorMethod.setAccessible(true);
                advisorMethod.invoke(getProxy(), getArgs());
            }
        }
    }
}
