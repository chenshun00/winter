package top.huzhurong.ioc.bean.processor;

import lombok.extern.slf4j.Slf4j;
import top.huzhurong.aop.advisor.pointcut.TransactionPointcut;
import top.huzhurong.aop.advisor.transaction.TransactionManager;
import top.huzhurong.aop.advisor.transaction.manager.JdbcTransactionManager;
import top.huzhurong.aop.core.StringUtil;
import top.huzhurong.ioc.annotation.EnableConfiguration;
import top.huzhurong.ioc.bean.ClassInfo;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * @author luobo.cs@raycloud.com
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
            ClassInfo classInfo = new ClassInfo(AopBeanProcessor.class, StringUtil.handleClassName(AopBeanProcessor.class));
            classInfoSet.add(classInfo);
        } else if ("transaction".equals(config)) {
            log.info("start handle transaction, add TransactionBeanProcessor.class");
            ClassInfo classInfo = new ClassInfo(TransactionBeanProcessor.class, StringUtil.handleClassName(TransactionBeanProcessor.class));
            ClassInfo transaction = new ClassInfo(JdbcTransactionManager.class, StringUtil.handleClassName(TransactionManager.class));
            ClassInfo pointCut = new ClassInfo(TransactionPointcut.class, StringUtil.handleClassName(TransactionPointcut.class));
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
