package top.huzhurong.ioc.bean.processor;

import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.advisor.pointcut.TransactionPointcut;
import top.huzhurong.aop.advisor.transaction.TransactionAdvisor;
import top.huzhurong.aop.advisor.transaction.TransactionManager;
import top.huzhurong.aop.invocation.ProxyFactory;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/9
 */
public class TransactionBeanProcessor extends AbstractBeanProcessor implements BeanProcessor {

    @Override
    protected Object processSubType(Object object) {
        List<Advisor> advisorsList = new LinkedList<>();
        TransactionAdvisor bean = this.getIocContainer().getBean(TransactionAdvisor.class);
        if (bean == null) {
            bean = dealTransactionAdvisor();
            this.getIocContainer().put("transactionAdvisor", bean);
        }
        for (Method declaredMethod : object.getClass().getDeclaredMethods()) {
            if (bean.getPointcut().match(declaredMethod)) {
                advisorsList.add(bean);
            }
        }
        if (advisorsList.size() == 0) {
            return object;
        } else {
            return ProxyFactory.newProxy(object, object.getClass(), advisorsList);
        }
    }


    public TransactionAdvisor dealTransactionAdvisor() {
        TransactionManager bean = (TransactionManager) this.getIocContainer().getBean("jdbcTransactionManager");
        TransactionAdvisor transactionAdvisor = new TransactionAdvisor(bean);
        transactionAdvisor.setPointcut(new TransactionPointcut());
        return transactionAdvisor;
    }
}
