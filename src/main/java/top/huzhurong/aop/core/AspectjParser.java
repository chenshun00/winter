package top.huzhurong.aop.core;

import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.advisor.AfterAdvisor;
import top.huzhurong.aop.advisor.AroundAdvisor;
import top.huzhurong.aop.advisor.BeforeAdvisor;
import top.huzhurong.aop.annotation.After;
import top.huzhurong.aop.annotation.Around;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.annotation.Before;
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
        return advisors;
    }

    public static Object findApplyAdvisor(Object object, List<Advisor> advisors) {
        List<Advisor> advisorsList = new LinkedList<>();
        for (Advisor advisor : advisors) {
            if (advisor instanceof BeforeAdvisor) {
                BeforeAdvisor beforeAdvisor = (BeforeAdvisor) advisor;
                Method[] declaredMethods = object.getClass().getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.getName().equals(beforeAdvisor.getPointCut())) {
                        advisorsList.add(advisor);
                    }
                }
            } else if (advisor instanceof AfterAdvisor) {
                AfterAdvisor afterAdvisor = (AfterAdvisor) advisor;
                Method[] declaredMethods = object.getClass().getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.getName().equals(afterAdvisor.getPointCut())) {
                        advisorsList.add(advisor);
                    }
                }
            } else if (advisor instanceof AroundAdvisor) {
                AroundAdvisor aroundAdvisor = (AroundAdvisor) advisor;
                Method[] declaredMethods = object.getClass().getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.getName().equals(aroundAdvisor.getPointCut())) {
                        advisorsList.add(advisor);
                    }
                }
            }
        }
        return proxyFactory.newProxy(object.getClass(), advisorsList);
    }

    public static boolean findAAnnotation(Class<?> targetClass, Class<? extends Annotation> annotation) {
        return targetClass.getAnnotation(annotation) != null;
    }

    public static Annotation findAAnnotationInUse(Method method, Class<? extends Annotation> annotation) {
        return method.getAnnotation(annotation);
    }

}
