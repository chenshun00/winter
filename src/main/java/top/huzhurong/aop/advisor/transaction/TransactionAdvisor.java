package top.huzhurong.aop.advisor.transaction;

import lombok.Getter;
import lombok.Setter;
import top.huzhurong.aop.advisor.MethodInterceptor;
import top.huzhurong.aop.advisor.transaction.manager.TransactionIntercepter;
import top.huzhurong.aop.invocation.Invocation;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * 事务增强器
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/8/29
 */
public class TransactionAdvisor implements MethodInterceptor {

    @Getter
    @Setter
    private Method method;

    @Getter
    @Setter
    private Object object;

    @Getter
    @Setter
    private int args;

    private TransactionIntercepter transactionIntercepter = new TransactionIntercepter();

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        return transactionIntercepter.doTransaction(invocation, method, object, args);
    }
}
