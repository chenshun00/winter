package io.github.chenshun00.aop.invocation;

import lombok.Data;
import io.github.chenshun00.aop.advisor.Advisor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/27
 */
@Data
public class JdkProxy implements InvocationHandler {


    private Object target;
    private Object proxy;
    private List<Advisor> advisors;

    public JdkProxy(Object target, Object proxy, List<Advisor> advisors) {
        this.target = target;
        this.proxy = proxy;
        this.advisors = advisors;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return new JdkInvocation(target, proxy, method, args, advisors).proceed();
    }
}
