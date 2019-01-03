package top.huzhurong.aop.advisor.transaction;

import lombok.Getter;
import lombok.Setter;
import top.huzhurong.aop.advisor.AbstractAdvisor;
import top.huzhurong.aop.advisor.MethodInterceptor;
import top.huzhurong.aop.advisor.pointcut.TransactionPointcut;
import top.huzhurong.aop.advisor.transaction.manager.TransactionInterceptor;
import top.huzhurong.aop.invocation.Invocation;
import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.bean.aware.InitAware;
import top.huzhurong.ioc.bean.aware.IocContainerAware;

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
