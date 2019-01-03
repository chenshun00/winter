package top.huzhurong.ioc.bean.aware;

import top.huzhurong.ioc.bean.IocContainer;

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
