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
    @SuppressWarnings("unchecked")
    private static <T> T createProxy(Class<T> tClass, List<Advisor> advisors) {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback((MethodInterceptor) (object, method, args, methodProxy) ->
                new CglibInvocation(object, method, args, methodProxy, advisors).proceed());
        enhancer.setCallbackFilter(method -> 0);
        enhancer.setSuperclass(tClass);
        return (T) enhancer.create();
    }

    @SuppressWarnings("unchecked")
    private static <T> T createJdkProxy(Class<T> tClass, List<Advisor> advisors) throws IllegalAccessException, InstantiationException {
        ClassLoader classLoader = tClass.getClassLoader();
        JdkProxy jdkProxy = new JdkProxy(tClass.newInstance(), advisors);
        Object target = Proxy.newProxyInstance(classLoader, tClass.getInterfaces(), jdkProxy);
        return (T) target;
    }

    public static <T> T newProxy(Class<T> aClass, List<Advisor> advisorsList) {
        if (aClass.getInterfaces().length == 0 && aClass.getSuperclass().equals(Object.class)) {
            return createProxy(aClass, advisorsList);
        } else if (aClass.getInterfaces().length != 0) {
            try {
                return createJdkProxy(aClass, advisorsList);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new RuntimeException(aClass + " 不能被cglib或者jdk代理!");
        }
    }
}
