package top.huzhurong.aop.advisor;

import lombok.Getter;
import lombok.Setter;
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
public class BeforeAdvisor implements MethodInterceptor {

    //链接点
    private String pointCut;
    @Getter
    @Setter
    private Method method;

    @Getter
    @Setter
    private Object object;

    public String getPointCut() {
        return pointCut;
    }

    public void setPointCut(String pointCut) {
        this.pointCut = pointCut;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        if (invocation instanceof CglibInvocation) {
            CglibInvocation cglibInvocation = (CglibInvocation) invocation;
            method.setAccessible(true);
            Object[] args = cglibInvocation.getArgs();
            method.invoke(object, args);
        }
        if (invocation instanceof JdkInvocation){
            JdkInvocation jdkInvocation = (JdkInvocation) invocation;
            method.setAccessible(true);
            Object[] args = jdkInvocation.getArgs();
            method.invoke(object, args);
        }
        return invocation.proceed();
    }
}
