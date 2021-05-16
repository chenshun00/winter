package io.github.chenshun00.ioc.bean.aware;

import io.github.chenshun00.ioc.bean.IocContainer;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/9
 */
public interface IocContainerAware extends Aware {
    /**
     * inject ioc container
     *
     * @param iocContainer the ioc container only one
     */
    void setIocContainer(IocContainer iocContainer);

}
