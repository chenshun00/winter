package top.huzhurong.ioc.bean.aware;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/27
 */
public interface FactoryBean<T> {

    T getObject();

    Class<?> getObjectType();

    default boolean isSingleton() {
        return true;
    }

}
