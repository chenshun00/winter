package io.github.chenshun00.ioc.bean.processor;

import io.github.chenshun00.ioc.annotation.Bean;
import io.github.chenshun00.ioc.annotation.Inject;
import io.github.chenshun00.ioc.bean.ClassInfo;
import io.github.chenshun00.ioc.bean.IocContainer;
import io.github.chenshun00.ioc.bean.aware.IocContainerAware;
import io.github.chenshun00.util.StringUtils;
import io.github.chenshun00.ioc.annotation.Configuration;
import io.github.chenshun00.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/16
 */
public class ConfigurationUtil implements IocContainerAware {

    private IocContainer iocContainer;

    private Set<ClassInfo> classInfos = new HashSet<>(32);

    private boolean flag = true;

    public void handleConfig(Set<ClassInfo> classInfoSet) {
        Set<ClassInfo> collect = classInfoSet.stream()
                .map(this::handle)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        classInfoSet.addAll(collect);
        handleLast(classInfoSet);
    }

    private void handleLast(Set<ClassInfo> classInfoSet) {
        flag = false;
        if (classInfos.size() != 0) {
            Set<ClassInfo> collect = classInfoSet.stream()
                    .map(this::handle)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            classInfoSet.addAll(collect);
        }
    }

    /**
     * @param classInfo bean
     * @return 注入的方法
     */
    private Set<ClassInfo> handle(ClassInfo classInfo) {
        Class<?> aClass = classInfo.getaClass();
        Set<ClassInfo> set = new HashSet<>();
        if (aClass.isAnnotationPresent(Configuration.class)) {
            //第一步:注入字段，把那些没有依赖的数据注入到ioc当中，需要要依赖随后注入
            if (flag) {
                Field[] fields = aClass.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Inject.class)) {
                        Object bean = this.iocContainer.getBean(field.getType());
                        if (bean == null) {
                            classInfos.add(classInfo);
                            return null;
                        } else {
                            ReflectUtils.setField(field, this.iocContainer.getBean(classInfo.getaClass()), bean);
                        }
                    }
                }
            }

            Method[] declaredMethods = aClass.getDeclaredMethods();
            Object bean = iocContainer.getBean(aClass);
            Stream.of(declaredMethods).forEach(method -> {
                if (method.isAnnotationPresent(Bean.class)) {
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

        iocContainer.put(StringUtils.handleClassName(returnType), invoke);
        return new ClassInfo(returnType, StringUtils.handleClassName(returnType));
    }

    @Override
    public void setIocContainer(IocContainer iocContainer) {
        this.iocContainer = iocContainer;
    }
}
