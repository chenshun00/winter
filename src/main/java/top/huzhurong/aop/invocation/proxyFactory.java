package top.huzhurong.aop.invocation;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import top.huzhurong.aop.advisor.Advisor;

import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
public class proxyFactory {
    @SuppressWarnings("unchecked")
    public static  <T> T createProxy(Class<T> tClass, List<Advisor> advisors) {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback((MethodInterceptor) (object, method, args, methodProxy) ->
                 new CglibInvocation(object, method, args, methodProxy, advisors).proceed());
        enhancer.setCallbackFilter(method -> 0);
        enhancer.setSuperclass(tClass);
        return (T) enhancer.create();
    }
}
