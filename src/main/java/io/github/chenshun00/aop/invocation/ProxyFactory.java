package io.github.chenshun00.aop.invocation;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import io.github.chenshun00.aop.advisor.Advisor;
import io.github.chenshun00.ioc.bean.processor.AopConfigUtil;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/26
 */
public class ProxyFactory {
    private static <T> T createProxy(Object target, Class<T> tClass, List<Advisor> advisors) {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback((MethodInterceptor) (object, method, args, methodProxy) ->
                new CglibInvocation(target, object, method, args, methodProxy, advisors).proceed());
        enhancer.setCallbackFilter(method -> 0);
        enhancer.setTarget(target);
        @SuppressWarnings("unchecked")
        T proxy = (T) enhancer.create();
        return proxy;
    }

    private static <T> T createJdkProxy(Object target, Class<T> tClass, List<Advisor> advisors) {
        ClassLoader classLoader = tClass.getClassLoader();
        JdkProxy jdkProxy = new JdkProxy(target, tClass, advisors);
        @SuppressWarnings("unchecked")
        T proxy = (T) Proxy.newProxyInstance(classLoader, tClass.getInterfaces(), jdkProxy);
        return proxy;
    }

    public static <T> T newProxy(Object target, Class<T> aClass, List<Advisor> advisorsList) {
        if (AopConfigUtil.proxyByClass) {
            return createProxy(target, aClass, advisorsList);
        }
        if (aClass.getInterfaces().length == 0 && aClass.getSuperclass().equals(Object.class)) {
            return createProxy(target, aClass, advisorsList);
        } else if (aClass.getInterfaces().length != 0) {
            return createJdkProxy(target, aClass, advisorsList);
        } else {
            throw new RuntimeException(aClass + " 不能被cglib或者jdk代理!");
        }
    }
}
