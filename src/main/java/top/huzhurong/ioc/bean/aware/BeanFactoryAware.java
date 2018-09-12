package top.huzhurong.ioc.bean.aware;

import top.huzhurong.ioc.bean.BeanFactory;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/12
 */
public interface BeanFactoryAware {

    void setBeanFactory(BeanFactory beanFactory);

}
