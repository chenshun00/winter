package top.huzhurong.aop.advisor;

import top.huzhurong.aop.invocation.CglibInvocation;
import top.huzhurong.aop.invocation.Invocation;
import top.huzhurong.aop.invocation.JdkInvocation;

import java.lang.reflect.Method;

/**
 * 后置增强
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
public class AfterAdvisor extends AbstractAdvisor implements MethodInterceptor {

    private Method method;
    private Object object;

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } finally {
            if (invocation instanceof CglibInvocation) {
                CglibInvocation cglibInvocation = (CglibInvocation) invocation;
                method.setAccessible(true);
                method.invoke(object, cglibInvocation.getArgs());
            } else if (invocation instanceof JdkInvocation) {
                JdkInvocation jdkInvocation = (JdkInvocation) invocation;
                method.setAccessible(true);
                method.invoke(object, jdkInvocation.getArgs());
            }
        }
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
