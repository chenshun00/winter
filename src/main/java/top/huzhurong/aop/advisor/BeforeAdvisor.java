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

    private Method method;
    private Object object;

    public Object invoke(Invocation invocation) throws Throwable {
        if (invocation instanceof CglibInvocation) {
            CglibInvocation cglibInvocation = (CglibInvocation) invocation;
            method.setAccessible(true);
            Object[] args = cglibInvocation.getArgs();
            method.invoke(object, args);
        }
        if (invocation instanceof JdkInvocation) {
            JdkInvocation jdkInvocation = (JdkInvocation) invocation;
            method.setAccessible(true);
            Object[] args = jdkInvocation.getArgs();
            method.invoke(object, args);
        }
        return invocation.proceed();
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
