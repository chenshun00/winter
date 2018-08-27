package top.huzhurong.aop.invocation;

import lombok.Getter;
import lombok.Setter;
import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.advisor.AfterAdvisor;
import top.huzhurong.aop.advisor.AroundAdvisor;
import top.huzhurong.aop.advisor.BeforeAdvisor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/27
 */

public class JdkInvocation implements Invocation {

    @Getter
    @Setter
    private Object target;
    @Getter
    @Setter
    private Object proxy;
    @Getter
    @Setter
    private Method method;
    @Getter
    @Setter
    private Object[] args;
    @Getter
    @Setter
    List<Advisor> advisors;

    public JdkInvocation(Object target, Method method, Object[] args, List<Advisor> advisors) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.advisors = advisors;
    }

    private int InvocationIndex = -1;

    @Override
    public Object proceed() throws Throwable {
        if (InvocationIndex == advisors.size() - 1) {
            return method.invoke(target, args);
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
        } else if (advisor instanceof AroundAdvisor) {
            AroundAdvisor aroundAdvisor = (AroundAdvisor) advisor;
            if (aroundAdvisor.getPointCut().equals(this.method.getName())) {
                return aroundAdvisor.invoke(this);
            } else {
                return proceed();
            }
        } else {
            return proceed();
        }
    }

}
