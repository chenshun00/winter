package top.huzhurong.ioc.bean.processor;

import top.huzhurong.ioc.annotation.Bean;
import top.huzhurong.ioc.annotation.Configuration;
import top.huzhurong.ioc.bean.ClassInfo;
import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.bean.aware.IocContainerAware;
import top.huzhurong.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/16
 */
public class ConfigurationUtil implements IocContainerAware {

    private IocContainer iocContainer;

    public void handleConfig(Set<ClassInfo> classInfoSet) {
        Set<ClassInfo> collect = classInfoSet.stream()
                .map(this::handle)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        classInfoSet.addAll(collect);

    }

    private Set<ClassInfo> handle(ClassInfo classInfo) {
        Class<?> aClass = classInfo.getaClass();
        Set<ClassInfo> set = new HashSet<>();
        Configuration declaredAnnotation = aClass.getDeclaredAnnotation(Configuration.class);
        if (declaredAnnotation != null) {
            Method[] declaredMethods = aClass.getDeclaredMethods();
            Object bean = iocContainer.getBean(aClass);
            Stream.of(declaredMethods).forEach(method -> {
                if (method.getDeclaredAnnotation(Bean.class) != null) {
                    set.add(handleMethod(method, bean));
                }
            });
        }
        return set;
    }

    private ClassInfo handleMethod(Method method, Object bean) {
        Class<?> returnType = method.getReturnType();
        Object invoke = null;
        try {
            invoke = method.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        iocContainer.put(StringUtil.handleClassName(returnType), invoke);
        return new ClassInfo(returnType, StringUtil.handleClassName(returnType));
    }

    @Override
    public void setIocContainer(IocContainer iocContainer) {
        this.iocContainer = iocContainer;
    }
}
