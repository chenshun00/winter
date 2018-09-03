package top.huzhurong.aop.advisor;

import top.huzhurong.aop.advisor.pointcut.Pointcut;
import top.huzhurong.aop.invocation.Invocation;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/3
 */
public abstract class AbstractAdvisor implements MethodInterceptor {

    protected Pointcut pointcut;

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        return null;
    }

    @Override
    public Pointcut getPointcut() {
        return null;
    }

    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }
}
