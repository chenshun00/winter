package top.huzhurong.aop.invocation;

import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.advisor.AfterAdvisor;
import top.huzhurong.aop.advisor.AroundAdvisor;
import top.huzhurong.aop.advisor.BeforeAdvisor;
import top.huzhurong.aop.advisor.transaction.TransactionAdvisor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/27
 */
public abstract class AbstractInvocation implements Invocation {
    private int InvocationIndex = -1;

    private Object target;
    private Object proxy;
    private Method method;
    private Object[] args;
    private List<Advisor> advisors;

    @Override
    public Object proceed() throws Throwable {
        if (InvocationIndex == advisors.size() - 1) {
            return invokePointCut();
        }
        Advisor advisor = advisors.get(++InvocationIndex);
        if (advisor instanceof BeforeAdvisor) {
            BeforeAdvisor beforeAdvisor = (BeforeAdvisor) advisor;
            if (beforeAdvisor.getPointcut().match(this.method)) {
                return beforeAdvisor.invoke(this);
            } else {
                return proceed();
            }
        } else if (advisor instanceof AfterAdvisor) {
            AfterAdvisor afterAdvisor = (AfterAdvisor) advisor;
            if (afterAdvisor.getPointcut().match(this.method)) {
                return afterAdvisor.invoke(this);
            } else {
                return proceed();
            }
        } else if (advisor instanceof AroundAdvisor) {
            AroundAdvisor aroundAdvisor = (AroundAdvisor) advisor;
            if (aroundAdvisor.getPointcut().match(this.method)) {
                return aroundAdvisor.invoke(this);
            } else {
                return proceed();
            }
        } else if (advisor instanceof TransactionAdvisor) {
            TransactionAdvisor transactionAdvisor = (TransactionAdvisor) advisor;
            if (transactionAdvisor.getPointcut().match(this.method)) {
                return transactionAdvisor.invoke(this);
            } else {
                return proceed();
            }
        } else {
            return proceed();
        }
    }


    public AbstractInvocation(Object target, Object proxy, Method method, Object[] args, List<Advisor> advisors) {
        this.target = target;
        this.proxy = proxy;
        this.method = method;
        this.args = args;
        this.advisors = advisors;
    }

    protected Object invokePointCut() throws Throwable {
        return method.invoke(target, args);
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
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

    public List<Advisor> getAdvisors() {
        return advisors;
    }

    public void setAdvisors(List<Advisor> advisors) {
        this.advisors = advisors;
    }
}
