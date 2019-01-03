package top.huzhurong.ioc.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/6
 */
public interface InfoBeanFactory extends BeanFactory {

    List<String> getBeanNameForType(Class<?> type);

    default <T> List<T> getBeanInstancesForType(Class<T> type) {
        return new ArrayList<>();
    }
}
