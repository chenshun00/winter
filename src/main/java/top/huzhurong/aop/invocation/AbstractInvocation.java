package top.huzhurong.aop.invocation;

import net.sf.cglib.proxy.MethodProxy;
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
public abstract class AbstractInvocation implements Invocation {
    private int InvocationIndex = -1;


    private Object target;
    private Method method;
    private Object[] args;
    private MethodProxy methodProxy;
    private List<Advisor> advisors;

    @Override
    public Object proceed() throws Throwable {
        if (InvocationIndex == advisors.size() - 1) {
            return invokePointCut();
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


    protected Object invokePointCut() throws Throwable {
        return method.invoke(target, args);
    }

    public AbstractInvocation(Object target, Method method, Object[] args, MethodProxy methodProxy, List<Advisor> advisors) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.methodProxy = methodProxy;
        this.advisors = advisors;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public void setMethodProxy(MethodProxy methodProxy) {
        this.methodProxy = methodProxy;
    }

    public List<Advisor> getAdvisors() {
        return advisors;
    }

    public void setAdvisors(List<Advisor> advisors) {
        this.advisors = advisors;
    }
}
