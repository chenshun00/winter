package top.huzhurong.aop.invocation;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import top.huzhurong.aop.advisor.Advisor;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
public class proxyFactory {
    private static <T> T createProxy(Object target, Class<T> tClass, List<Advisor> advisors) {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback((MethodInterceptor) (object, method, args, methodProxy) ->
                new CglibInvocation(target, object, method, args, methodProxy, advisors).proceed());
        enhancer.setCallbackFilter(method -> 0);
        enhancer.setSuperclass(tClass);
        @SuppressWarnings("unchecked")
        T proxy = (T) enhancer.create();
        return proxy;
    }

    private static <T> T createJdkProxy(Object target, Class<T> tClass, List<Advisor> advisors) throws IllegalAccessException, InstantiationException {
        ClassLoader classLoader = tClass.getClassLoader();
        JdkProxy jdkProxy = new JdkProxy(target, tClass, advisors);
        @SuppressWarnings("unchecked")
        T proxy = (T) Proxy.newProxyInstance(classLoader, tClass.getInterfaces(), jdkProxy);
        return proxy;
    }

    public static <T> T newProxy(Object target, Class<T> aClass, List<Advisor> advisorsList) {
        if (aClass.getInterfaces().length == 0 && aClass.getSuperclass().equals(Object.class)) {
            return createProxy(target, aClass, advisorsList);
        } else if (aClass.getInterfaces().length != 0) {
            try {
                return createJdkProxy(target, aClass, advisorsList);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new RuntimeException(aClass + " 不能被cglib或者jdk代理!");
        }
    }
}
