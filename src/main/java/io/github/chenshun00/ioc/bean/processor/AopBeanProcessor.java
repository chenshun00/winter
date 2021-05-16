package io.github.chenshun00.ioc.bean.processor;

import io.github.chenshun00.aop.invocation.ProxyFactory;
import io.github.chenshun00.aop.advisor.Advisor;
import io.github.chenshun00.aop.advisor.AfterAdvisor;
import io.github.chenshun00.aop.advisor.AroundAdvisor;
import io.github.chenshun00.aop.advisor.BeforeAdvisor;
import io.github.chenshun00.aop.core.AspectjParser;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/9
 */
public class AopBeanProcessor extends AbstractBeanProcessor implements BeanProcessor {

    /**
     * @param object origin bean or target
     * @return proxy
     */
    @Override
    protected Object processSubType(Object object) {
        List<Object> aspectj = getIocContainer().aspectj();
        return findApplyAdvisor(object, AspectjParser.parserAspectj(aspectj));
    }


    private Object findApplyAdvisor(Object object, List<Advisor> advisors) {
        List<Advisor> advisorsList = new LinkedList<>();
        Method[] declaredMethods = object.getClass().getDeclaredMethods();
        for (Advisor advisor : advisors) {
            if (advisor instanceof BeforeAdvisor) {
                BeforeAdvisor beforeAdvisor = (BeforeAdvisor) advisor;
                for (Method declaredMethod : declaredMethods) {
                    if (beforeAdvisor.getPointcut().match(declaredMethod)) {
                        advisorsList.add(advisor);
                    }
                }
            } else if (advisor instanceof AfterAdvisor) {
                AfterAdvisor afterAdvisor = (AfterAdvisor) advisor;
                for (Method declaredMethod : declaredMethods) {
                    if (afterAdvisor.getPointcut().match(declaredMethod)) {
                        advisorsList.add(advisor);
                    }
                }
            } else if (advisor instanceof AroundAdvisor) {
                AroundAdvisor aroundAdvisor = (AroundAdvisor) advisor;
                for (Method declaredMethod : declaredMethods) {
                    if (aroundAdvisor.getPointcut().match(declaredMethod)) {
                        advisorsList.add(advisor);
                    }
                }
            }
        }
        if (advisorsList.size() == 0) {
            return object;
        } else {
            return ProxyFactory.newProxy(object, object.getClass(), advisorsList);
        }
    }
}
