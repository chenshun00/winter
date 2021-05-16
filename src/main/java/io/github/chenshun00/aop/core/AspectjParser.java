package io.github.chenshun00.aop.core;

import io.github.chenshun00.aop.annotation.After;
import io.github.chenshun00.aop.annotation.Around;
import io.github.chenshun00.aop.annotation.Aspectj;
import io.github.chenshun00.aop.annotation.Before;
import io.github.chenshun00.aop.invocation.ProxyFactory;
import io.github.chenshun00.aop.advisor.Advisor;
import io.github.chenshun00.aop.advisor.AfterAdvisor;
import io.github.chenshun00.aop.advisor.AroundAdvisor;
import io.github.chenshun00.aop.advisor.BeforeAdvisor;
import io.github.chenshun00.aop.advisor.pointcut.StringPointcut;
import io.github.chenshun00.aop.advisor.transaction.TransactionAdvisor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * 解析切面
 *
 * @author chenshun00@gmail.com
 * @since 2018/8/26
 */
public class AspectjParser {

    /**
     * execute order , sort by @order annotation , default value is {@link Integer#MAX_VALUE}
     * now the ioc container does not exist ， so we can't get aspectj set from ioc
     *
     * @param aspectjList set of aspectj
     * @return advisor list
     */
    public static List<Advisor> parserAspectj(List<Object> aspectjList) {
        aspectjList.sort(new OrderConparator());
        List<Advisor> advisorList = new LinkedList<>();
        aspectjList.forEach(as -> advisorList.addAll(parserAspectj(as.getClass(), as)));
        return advisorList;
    }

    /**
     * 解析切面
     *
     * @param aClass aspectj bean
     * @return advisor list
     */
    public static List<Advisor> parserAspectj(Class<?> aClass, Object object) {
        if (aClass == null) {
            throw new NullPointerException();
        }
        List<Advisor> advisors = new LinkedList<>();
        if (!findAAnnotation(aClass, Aspectj.class)) {
            return advisors;
        }
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            findAAnnotationInUse(declaredMethod, Before.class).ifPresent(annotation -> {
                BeforeAdvisor beforeAdvisor = new BeforeAdvisor(declaredMethod, object, declaredMethod.getParameterTypes(), new StringPointcut(((Before) annotation).value()));
                advisors.add(beforeAdvisor);
            });
            findAAnnotationInUse(declaredMethod, After.class).ifPresent(after -> {
                AfterAdvisor afterAdvisor = new AfterAdvisor(declaredMethod, object, declaredMethod.getParameterTypes(), new StringPointcut(((After) after).value()));
                advisors.add(afterAdvisor);
            });
            findAAnnotationInUse(declaredMethod, Around.class).ifPresent(around -> {
                AroundAdvisor aroundAdvisor = new AroundAdvisor(declaredMethod, object, declaredMethod.getParameterTypes(), new StringPointcut(((Around) around).value()));
                advisors.add(aroundAdvisor);
            });
        }
        return advisors;
    }

    /**
     * 查看对象是否被切面拦截，目前未实现 [* package.class.method (**)] 的切面规则
     *
     * @param object   special bean
     * @param advisors aspectj set
     */
    public static Object findApplyAdvisor(Object object, List<Advisor> advisors) {
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
            } else if (advisor instanceof TransactionAdvisor) {
                TransactionAdvisor transactionAdvisor = (TransactionAdvisor) advisor;
                for (Method declaredMethod : declaredMethods) {
                    if (transactionAdvisor.getPointcut().match(declaredMethod)) {
                        transactionAdvisor.setProxy(object);
                        advisorsList.add(transactionAdvisor);
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

    /**
     * @param targetClass target class
     * @param annotation  special annotation
     * @return return true if exist , or false
     */
    public static boolean findAAnnotation(Class<?> targetClass, Class<? extends Annotation> annotation) {
        return targetClass.getAnnotation(annotation) != null;
    }

    /**
     * @param method     target method
     * @param annotation special annotation
     * @return Optional
     */
    public static Optional<Annotation> findAAnnotationInUse(Method method, Class<? extends Annotation> annotation) {
        Annotation declaredAnnotation = method.getDeclaredAnnotation(annotation);
        if (declaredAnnotation == null) {
            return Optional.empty();
        } else {
            return Optional.of(declaredAnnotation);
        }
    }

}
