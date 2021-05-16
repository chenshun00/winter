package io.github.chenshun00.ioc.bean.aware;

import io.github.chenshun00.ioc.bean.BeanFactory;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/12
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory);

}
