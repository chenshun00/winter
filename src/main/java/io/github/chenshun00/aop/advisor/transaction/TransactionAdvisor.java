package io.github.chenshun00.aop.advisor.transaction;

import io.github.chenshun00.aop.advisor.AbstractAdvisor;
import io.github.chenshun00.aop.advisor.MethodInterceptor;
import io.github.chenshun00.aop.advisor.transaction.manager.TransactionInterceptor;
import io.github.chenshun00.aop.invocation.Invocation;
import io.github.chenshun00.ioc.bean.IocContainer;
import io.github.chenshun00.ioc.bean.aware.InitAware;
import io.github.chenshun00.ioc.bean.aware.IocContainerAware;
import lombok.Getter;
import lombok.Setter;
import io.github.chenshun00.aop.advisor.pointcut.TransactionPointcut;

/**
 * 事务增强器
 *
 * @author chenshun00@gmail.com
 * @since 2018/8/29
 */
public class TransactionAdvisor extends AbstractAdvisor implements MethodInterceptor, IocContainerAware, InitAware {

    @Getter
    @Setter
    private Object object;

    @Getter
    @Setter
    private TransactionManager transactionManager;

    private IocContainer iocContainer;

    private TransactionInterceptor transactionInterceptor;

    public TransactionAdvisor(TransactionManager transactionManager) {
        super(null, null, null, new TransactionPointcut());
        this.transactionManager = transactionManager;
        this.transactionInterceptor = new TransactionInterceptor(this.transactionManager);
    }

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        return transactionInterceptor.doTransaction(invocation, getObject());
    }

    @Override
    public void setIocContainer(IocContainer iocContainer) {
        this.iocContainer = iocContainer;
    }

    @Override
    public void initBean() {
        transactionManager = iocContainer.getBean(TransactionManager.class);
    }

}
