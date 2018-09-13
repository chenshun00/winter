package top.huzhurong.ioc.bean.processor;

import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.advisor.AfterAdvisor;
import top.huzhurong.aop.advisor.AroundAdvisor;
import top.huzhurong.aop.advisor.BeforeAdvisor;
import top.huzhurong.aop.core.AspectjParser;
import top.huzhurong.aop.invocation.ProxyFactory;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author luobo.cs@raycloud.com
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
