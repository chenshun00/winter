package io.github.chenshun00.ioc.bean;

import java.util.List;
import java.util.Set;

/**
 * bean factory ， create bean to context or receive bean form context
 *
 * @author chenshun00@gmail.com
 * @since 2018/9/6
 */
public interface BeanFactory {

    Object getBean(String name);

    <T> T getBean(Class<T> tClass);

    <T> T getBean(String name, Class<T> tClass);

    Set<ClassInfo> register(Set<ClassInfo> classInfoSet);

    void register(ClassInfo classInfo);

    void put(String name, Object object);

    List<Object> aspectj();
}
