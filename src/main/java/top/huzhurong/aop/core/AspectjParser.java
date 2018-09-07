package top.huzhurong.aop.core;

import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.advisor.AfterAdvisor;
import top.huzhurong.aop.advisor.AroundAdvisor;
import top.huzhurong.aop.advisor.BeforeAdvisor;
import top.huzhurong.aop.advisor.pointcut.StringPointcut;
import top.huzhurong.aop.advisor.pointcut.TransactionPointcut;
import top.huzhurong.aop.advisor.transaction.TransactionAdvisor;
import top.huzhurong.aop.advisor.transaction.TransactionManager;
import top.huzhurong.aop.advisor.transaction.manager.JdbcTransactionManager;
import top.huzhurong.aop.annotation.*;
import top.huzhurong.aop.invocation.proxyFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * 解析切面
 *
 * @author luobo.cs@raycloud.com
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
        aspectjList.sort((aspectj1, aspectj2) -> {
            Order first = aspectj1.getClass().getAnnotation(Order.class);
            Order second = aspectj2.getClass().getAnnotation(Order.class);
            int firstOrder = first == null ? Integer.MAX_VALUE : first.value();
            int secondOrder = second == null ? Integer.MAX_VALUE : second.value();
            return Integer.compare(firstOrder, secondOrder);
        });
        List<Advisor> advisorList = new LinkedList<>();
        aspectjList.forEach(as -> advisorList.addAll(parserAspectj(as.getClass(), as)));
        advisorList.addAll(dealTransactionAdvisor());
        return advisorList;
    }

    /**
     * 解析切面，尚未实现ioc只能采取目前之中方法，如果实现了ioc就可以添加 beanProcessor 前后置处理
     *
     * @param aClass 切面对象
     * @return 切面集合
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
     * Temporary processing transaction , if we add ioc and more feature will add a flag to support if we use transaction
     */
    private static List<Advisor> dealTransactionAdvisor() {
        List<Advisor> advisors = new LinkedList<>();
        TransactionManager transactionManager = new JdbcTransactionManager();
        TransactionAdvisor transactionAdvisor = new TransactionAdvisor(transactionManager);
        transactionAdvisor.setPointcut(new TransactionPointcut());
        advisors.add(transactionAdvisor);
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
            return proxyFactory.newProxy(object, object.getClass(), advisorsList);
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
    public static Optional<Annotation> findAAnnotationInUse(Method method, Class<? extends Annotation> annotation) {
        Annotation declaredAnnotation = method.getDeclaredAnnotation(annotation);
        if (declaredAnnotation == null) {
            return Optional.empty();
        } else {
            return Optional.of(declaredAnnotation);
        }
    }

}
