package top.huzhurong.ioc.bean.processor;

import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.advisor.transaction.TransactionAdvisor;
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
        for (String beanName : this.beanNameForType()) {
            TransactionAdvisor transactionAdvisor = (TransactionAdvisor) this.getIocContainer().getBean(beanName);
            for (Method declaredMethod : object.getClass().getDeclaredMethods()) {
                if (transactionAdvisor.getPointcut().match(declaredMethod)) {
                    advisorsList.add(transactionAdvisor);
                }
            }
        }
        if (advisorsList.size() == 0) {
            return object;
        } else {
            return ProxyFactory.newProxy(object, object.getClass(), advisorsList);
        }
    }

    @Override
    protected List<String> beanNameForType() {
        return this.getIocContainer().getBeanNameForType(TransactionAdvisor.class);
    }
}
