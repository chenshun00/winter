package top.huzhurong.aop.core;

import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.advisor.AfterAdvisor;
import top.huzhurong.aop.advisor.AroundAdvisor;
import top.huzhurong.aop.advisor.BeforeAdvisor;
import top.huzhurong.aop.advisor.transaction.TransactionAdvisor;
import top.huzhurong.aop.annotation.*;
import top.huzhurong.aop.invocation.proxyFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 解析切面
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
public class AspectjParser {

    /**
     * 解析切面，尚未实现ioc只能采取目前之中方法，如果实现了ioc就可以添加 beanProcessor 前后置处理
     *
     * @param aClass 切面对象
     * @return 切面集合
     */
    public static List<Advisor> parserAspectj(Class<?> aClass) throws IllegalAccessException, InstantiationException {
        if (aClass == null) {
            throw new NullPointerException();
        }
        if (!findAAnnotation(aClass, Aspectj.class)) {
            return new LinkedList<>();
        }
        Method[] declaredMethods = aClass.getDeclaredMethods();
        List<Advisor> advisors = new LinkedList<Advisor>();
        for (Method declaredMethod : declaredMethods) {
            Before before = (Before) findAAnnotationInUse(declaredMethod, Before.class);
            if (before != null) {
                //暂时将pointCut定义为切点
                String pointCut = before.value();
                BeforeAdvisor beforeAdvisor = new BeforeAdvisor();
                beforeAdvisor.setObject(aClass.newInstance());
                beforeAdvisor.setMethod(declaredMethod);
                beforeAdvisor.setPointCut(pointCut);
                advisors.add(beforeAdvisor);
            }
            After after = (After) findAAnnotationInUse(declaredMethod, After.class);
            if (after != null) {
                String pointCut = after.value();
                AfterAdvisor afterAdvisor = new AfterAdvisor();
                afterAdvisor.setObject(aClass.newInstance());
                afterAdvisor.setMethod(declaredMethod);
                afterAdvisor.setPointCut(pointCut);
                advisors.add(afterAdvisor);
            }
            Around around = (Around) findAAnnotationInUse(declaredMethod, Around.class);
            if (around != null) {
                String pointCut = around.value();
                AroundAdvisor aroundAdvisor = new AroundAdvisor();
                aroundAdvisor.setObject(aClass.newInstance());
                aroundAdvisor.setMethod(declaredMethod);
                aroundAdvisor.setPointCut(pointCut);
                aroundAdvisor.setArgs(declaredMethod.getParameterCount());
                advisors.add(aroundAdvisor);
            }
        }
        advisors.add(new TransactionAdvisor());
        return advisors;
    }

    /**
     * 查看对象是否被切面拦截，目前未实现 [* package.class.method (**)] 的切面规则
     *
     * @param object   ioc 容器对象(实例化对象，未实现ioc之前就是自己new的实例对象)
     * @param advisors 切面集合
     */
    public static Object findApplyAdvisor(Object object, List<Advisor> advisors) {
        List<Advisor> advisorsList = new LinkedList<>();
        for (Advisor advisor : advisors) {
            Method[] declaredMethods = object.getClass().getDeclaredMethods();

            if (advisor instanceof BeforeAdvisor) {
                BeforeAdvisor beforeAdvisor = (BeforeAdvisor) advisor;
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.getName().equals(beforeAdvisor.getPointCut())) {
                        advisorsList.add(advisor);
                    }
                }
            } else if (advisor instanceof AfterAdvisor) {
                AfterAdvisor afterAdvisor = (AfterAdvisor) advisor;
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.getName().equals(afterAdvisor.getPointCut())) {
                        advisorsList.add(advisor);
                    }
                }
            } else if (advisor instanceof AroundAdvisor) {
                AroundAdvisor aroundAdvisor = (AroundAdvisor) advisor;
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.getName().equals(aroundAdvisor.getPointCut())) {
                        advisorsList.add(advisor);
                    }
                }
            } else if (advisor instanceof TransactionAdvisor) {
                TransactionAdvisor transactionAdvisor = (TransactionAdvisor) advisor;
                for (Method declaredMethod : declaredMethods) {
                    if (findAAnnotationInUse(declaredMethod, Transactional.class) != null) {
                        advisorsList.add(transactionAdvisor);
                    }
                }
            }
        }
        if (advisorsList.size() == 0) {
            return object;
        } else {
            return proxyFactory.newProxy(object.getClass(), advisorsList);
        }
    }

    /**
     * @param targetClass 目标类
     * @param annotation  注解
     * @return 存在该注解返回true，不存在返回false
     */
    public static boolean findAAnnotation(Class<?> targetClass, Class<? extends Annotation> annotation) {
        return targetClass.getAnnotation(annotation) != null;
    }

    /**
     * @param method     方法，public修饰符
     * @param annotation 注解
     * @return 注解
     */
    public static Annotation findAAnnotationInUse(Method method, Class<? extends Annotation> annotation) {
        return method.getAnnotation(annotation);
    }

}
