package top.huzhurong.aop.invocation;

import top.huzhurong.aop.advisor.Advisor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/27
 */

public class JdkInvocation extends AbstractInvocation implements Invocation {

    public JdkInvocation(Object target, Method method, Object[] args, List<Advisor> advisors) {
        super(target, method, args, null, advisors);
    }
}
