package top.huzhurong.ioc.bean;

import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/6
 */
public interface IocContainer {

    Object getBean(String name);

    <T> T getBean(Class<T> tClass);

    <T> T getBean(String name, Class<T> tClass);

    List<String> getBeanNameForType(Class<?> tClass);

    void put(String name, Object object);

    List<Object> aspectj();
}
