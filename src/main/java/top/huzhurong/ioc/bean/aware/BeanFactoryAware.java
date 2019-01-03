package top.huzhurong.ioc.bean.aware;

import top.huzhurong.ioc.bean.BeanFactory;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/12
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory);

}
