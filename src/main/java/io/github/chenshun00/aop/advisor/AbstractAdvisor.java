package io.github.chenshun00.aop.advisor;

import io.github.chenshun00.aop.invocation.Invocation;
import io.github.chenshun00.aop.advisor.pointcut.Pointcut;

import java.lang.reflect.Method;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/3
 */
public abstract class AbstractAdvisor implements MethodInterceptor {

    /**
     * 增强方法
     */
    protected Method advisorMethod;
    protected Object proxy;
    protected Object[] args;
    protected Pointcut pointcut;

    public AbstractAdvisor(Method advisorMethod, Object proxy, Object[] args, Pointcut pointcut) {
        this.advisorMethod = advisorMethod;
        this.proxy = proxy;
        this.args = args;
        this.pointcut = pointcut;
    }

    @Override
    public abstract Object invoke(Invocation invocation) throws Throwable;

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public Method getAdvisorMethod() {
        return advisorMethod;
    }

    public void setAdvisorMethod(Method advisorMethod) {
        this.advisorMethod = advisorMethod;
    }
}
