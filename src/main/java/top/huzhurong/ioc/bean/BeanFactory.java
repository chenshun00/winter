package top.huzhurong.ioc.bean;

/**
 * bean factory ， create bean to context or receive bean form context
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/9/6
 */
public interface BeanFactory {

    <T> T getBean(String name);

    <T> T getBean(Class<T> tClass);

    <T> T getBean(String name, Class<T> tClass);
}
