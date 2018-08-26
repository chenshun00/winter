package top.huzhurong.aop.invocation;

import lombok.Data;
import net.sf.cglib.proxy.MethodProxy;
import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.advisor.AfterAdvisor;
import top.huzhurong.aop.advisor.BeforeAdvisor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
@Data
public class CglibInvocation implements Invocation {
    private Object target;
    private Method method;
    private Object[] args;
    private MethodProxy methodProxy;
    private List<Advisor> advisors;

    private int InvocationIndex = -1;

    public CglibInvocation(Object target, Method method, Object[] args, MethodProxy methodProxy, List<Advisor> advisors) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.methodProxy = methodProxy;
        this.advisors = advisors;
    }


    public Object proceed() throws Throwable {
        if (InvocationIndex == advisors.size() - 1) {
            return methodProxy.invokeSuper(target, args);
        }
        Advisor advisor = advisors.get(++InvocationIndex);
        if (advisor instanceof BeforeAdvisor) {
            BeforeAdvisor beforeAdvisor = (BeforeAdvisor) advisor;
            if (beforeAdvisor.getPointCut().equals(this.method.getName())) {
                return beforeAdvisor.invoke(this);
            } else {
                return proceed();
            }
        } else if (advisor instanceof AfterAdvisor) {
            AfterAdvisor afterAdvisor = (AfterAdvisor) advisor;
            if (afterAdvisor.getPointCut().equals(this.method.getName())) {
                return afterAdvisor.invoke(this);
            } else {
                return proceed();
            }
        } else {
            return proceed();
        }
    }
}
