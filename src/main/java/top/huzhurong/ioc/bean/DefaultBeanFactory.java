package top.huzhurong.ioc.bean;

import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/6
 */
public class DefaultBeanFactory implements InfoBeanFactory {


    @Override
    public <T> T getBean(String name) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> tClass) {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> tClass) {
        return null;
    }

    @Override
    public List<String> getBeanNameForType(Class<?> type) {
        return null;
    }
}
