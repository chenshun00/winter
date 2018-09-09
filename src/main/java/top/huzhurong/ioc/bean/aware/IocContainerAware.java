package top.huzhurong.ioc.bean.aware;

import top.huzhurong.ioc.bean.IocContainer;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/9
 */
public interface IocContainerAware {
    /**
     * inject ioc container
     *
     * @param iocContainer the ioc container only one
     */
    void setIocContainer(IocContainer iocContainer);

}
