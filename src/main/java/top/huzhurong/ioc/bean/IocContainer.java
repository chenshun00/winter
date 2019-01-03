package top.huzhurong.ioc.bean;

import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/6
 */
public interface IocContainer extends InfoBeanFactory {

    List<String> beanNamesList();

}
