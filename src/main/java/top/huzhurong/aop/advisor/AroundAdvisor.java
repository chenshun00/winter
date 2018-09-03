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
        if (invocation instanceof CglibInvocation || invocation instanceof JdkInvocation) {
            method.setAccessible(true);
            if (args == 0) {
                return method.invoke(object);
            } else {
                return method.invoke(object, invocation);
            }
        } else {
            throw new IllegalAccessException("参数非法!");
        }
    }

    private Method method;

    private Object object;

    private int args;

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

    public int getArgs() {
        return args;
    }

    public void setArgs(int args) {
        this.args = args;
    }
}
