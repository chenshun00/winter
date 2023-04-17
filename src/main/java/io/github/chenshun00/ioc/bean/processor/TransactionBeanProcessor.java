package io.github.chenshun00.ioc.bean.processor;

import io.github.chenshun00.aop.advisor.Advisor;
import io.github.chenshun00.aop.advisor.pointcut.TransactionPointcut;
import io.github.chenshun00.aop.advisor.transaction.TransactionAdvisor;
import io.github.chenshun00.aop.advisor.transaction.TransactionManager;
import io.github.chenshun00.aop.annotation.Transactional;
import io.github.chenshun00.aop.invocation.ProxyFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/9
 */
public class TransactionBeanProcessor extends AbstractBeanProcessor implements BeanProcessor {

    @Override
    protected Object processSubType(Object object) {
        List<Advisor> advisorsList = new LinkedList<>();
        TransactionAdvisor bean = dealTransactionAdvisor(object);
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

    @Override
    protected boolean isInfrastructure(Object object) {
        final boolean annotationPresent = object.getClass().isAnnotationPresent(Transactional.class);
        if (annotationPresent) {
            return false;
        }
        for (Method declaredMethod : object.getClass().getDeclaredMethods()) {
            if (declaredMethod.isSynthetic()) {
                continue;
            }
            final boolean aPublic = Modifier.isPublic(declaredMethod.getModifiers());
            if (!aPublic) {
                continue;
            }
            return false;
        }
        return true;
    }

    public TransactionAdvisor dealTransactionAdvisor(Object object) {
        TransactionManager bean = (TransactionManager) this.getIocContainer().getBean("jdbcTransactionManager");
        TransactionPointcut pointcut = (TransactionPointcut) this.getIocContainer().getBean("transactionPointcut");
        TransactionAdvisor transactionAdvisor = new TransactionAdvisor(bean);
        transactionAdvisor.setObject(object);
        transactionAdvisor.setPointcut(pointcut);
        return transactionAdvisor;
    }
}
