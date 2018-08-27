package top.huzhurong.aop.advisor;

import lombok.Getter;
import lombok.Setter;
import top.huzhurong.aop.invocation.CglibInvocation;
import top.huzhurong.aop.invocation.Invocation;

import java.lang.reflect.Method;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
public class AroundAdvisor implements MethodInterceptor {

    @Getter
    @Setter
    private String pointCut;
    @Getter
    @Setter
    private Method method;

    @Getter
    @Setter
    private Object object;

    @Getter
    @Setter
    private int args;

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        if (invocation instanceof CglibInvocation) {
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
}
