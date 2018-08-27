package top.huzhurong.aop.advisor;

import top.huzhurong.aop.invocation.Invocation;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
public interface MethodInterceptor extends Advisor {
    Object invoke(Invocation invocation) throws Throwable;
}
