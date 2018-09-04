package top.huzhurong.aop.advisor;

import top.huzhurong.aop.invocation.CglibInvocation;
import top.huzhurong.aop.invocation.Invocation;
import top.huzhurong.aop.invocation.JdkInvocation;

import java.lang.reflect.Method;

/**
 * 环绕增强
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
public class AroundAdvisor extends AbstractAdvisor implements MethodInterceptor {

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        if (invocation instanceof CglibInvocation) {
            CglibInvocation cglibInvocation = (CglibInvocation) invocation;
            Method method = cglibInvocation.getMethod();
            return handle(method, cglibInvocation.getTarget(), invocation);
        } else if (invocation instanceof JdkInvocation) {
            JdkInvocation jdkInvocation = (JdkInvocation) invocation;
            Method method = jdkInvocation.getMethod();
            return handle(method, jdkInvocation.getTarget(), invocation);
        } else {
            throw new IllegalAccessException("参数非法!");
        }
    }

    private Object handle(Method method, Object target, Invocation invocation) throws Throwable {
        method.setAccessible(true);
        if (args == 0) {
            return method.invoke(target);
        } else {
            if (invocation == null) {
                throw new IllegalStateException("切面处理状态异常，可能是个bug");
            }
            return method.invoke(target, invocation);
        }
    }

    private int args;

    public int getArgs() {
        return args;
    }

    public void setArgs(int args) {
        this.args = args;
    }
}
