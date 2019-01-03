package top.huzhurong.aop.invocation;

import net.sf.cglib.proxy.MethodProxy;
import top.huzhurong.aop.advisor.Advisor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/26
 */
public class CglibInvocation extends AbstractInvocation implements Invocation {

    private Boolean isPublic;
    private MethodProxy methodProxy;

    public CglibInvocation(Object target, Object proxy, Method method, Object[] args, MethodProxy methodProxy, List<Advisor> advisors) {
        super(target, proxy, method, args, advisors);
        isPublic = Modifier.isPublic(method.getModifiers());
        this.methodProxy = methodProxy;
    }

    @Override
    protected Object invokePointCut() throws Throwable {
        if (isPublic) {
            return this.methodProxy.invokeSuper(this.getProxy(), getArgs());
        }
        return super.invokePointCut();
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public void setMethodProxy(MethodProxy methodProxy) {
        this.methodProxy = methodProxy;
    }
}
