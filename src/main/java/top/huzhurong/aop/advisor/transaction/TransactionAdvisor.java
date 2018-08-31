package top.huzhurong.aop.advisor.transaction;

import top.huzhurong.aop.advisor.MethodInterceptor;
import top.huzhurong.aop.invocation.Invocation;

/**
 * 事务增强器
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/8/29
 */
public class TransactionAdvisor implements MethodInterceptor {

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        return null;
    }
}
