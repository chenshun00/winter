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
public class AfterAdvisor implements MethodIntecepter {
    @Getter
    @Setter
    private String pointCut;
    @Getter
    @Setter
    private Method method;
    @Getter
    @Setter
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
            }
        }
    }
}
