package io.github.chenshun00.ioc.bean.processor;

import io.github.chenshun00.ioc.bean.ClassInfo;
import io.github.chenshun00.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import io.github.chenshun00.aop.advisor.pointcut.TransactionPointcut;
import io.github.chenshun00.aop.advisor.transaction.TransactionManager;
import io.github.chenshun00.aop.advisor.transaction.manager.JdbcTransactionManager;
import io.github.chenshun00.ioc.annotation.EnableConfiguration;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/13
 */
@Slf4j
public class AopConfigUtil {

    public static boolean proxyByClass = true;

    public static void handleConfig(Set<ClassInfo> classInfoSet, Class<?> bootClass) {
        Class<?> aClass = Objects.requireNonNull(bootClass, "bootClass can't be null");
        EnableConfiguration declaredAnnotation = aClass.getDeclaredAnnotation(EnableConfiguration.class);
        if (declaredAnnotation != null) {
            proxyByClass = declaredAnnotation.proxyByClass();
            Arrays.stream(declaredAnnotation.value())
                    .filter(str -> !str.isEmpty())
                    .forEach(config -> AopConfigUtil.doConfig(config, classInfoSet));
        }
    }

    private static void doConfig(String config, Set<ClassInfo> classInfoSet) {
        if ("aop".equals(config)) {
            log.info("start handle aop, add AopBeanProcessor.class");
            ClassInfo classInfo = new ClassInfo(AopBeanProcessor.class, StringUtils.handleClassName(AopBeanProcessor.class));
            classInfoSet.add(classInfo);
        } else if ("transaction".equals(config)) {
            log.info("start handle transaction, add TransactionBeanProcessor.class");
            ClassInfo classInfo = new ClassInfo(TransactionBeanProcessor.class, StringUtils.handleClassName(TransactionBeanProcessor.class));
            ClassInfo transaction = new ClassInfo(JdbcTransactionManager.class, StringUtils.handleClassName(TransactionManager.class));
            ClassInfo pointCut = new ClassInfo(TransactionPointcut.class, StringUtils.handleClassName(TransactionPointcut.class));
            classInfoSet.add(pointCut);
            classInfoSet.add(classInfo);
            classInfoSet.add(transaction);
        }
    }


    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    /**
     * Check whether the specified class name is a CGLIB-generated class.
     *
     * @param className the class name to check
     */
    public static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains("$$"));
    }

}
