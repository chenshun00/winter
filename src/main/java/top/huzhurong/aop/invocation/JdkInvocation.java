package top.huzhurong.aop.invocation;

import top.huzhurong.aop.advisor.Advisor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/27
 */

public class JdkInvocation extends AbstractInvocation implements Invocation {

    public JdkInvocation(Object target, Object proxy, Method method, Object[] args, List<Advisor> advisors) {
        super(target, proxy, method, args, advisors);
    }
}
