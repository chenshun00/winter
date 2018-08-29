package top.huzhurong.aop.invocation;

import net.sf.cglib.proxy.MethodProxy;
import top.huzhurong.aop.advisor.Advisor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
public class CglibInvocation extends AbstractInvocation implements Invocation {

    private Boolean isPublic;

    public CglibInvocation(Object target, Method method, Object[] args, MethodProxy methodProxy, List<Advisor> advisors) {
        super(target, method, args, methodProxy, advisors);
        isPublic = Modifier.isPublic(method.getModifiers());
    }

    @Override
    protected Object invokePointCut() throws Throwable {
        if (isPublic) {
            return getMethodProxy().invokeSuper(getTarget(), getArgs());
        }
        return super.invokePointCut();
    }
}
