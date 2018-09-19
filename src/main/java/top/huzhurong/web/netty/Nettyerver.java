package top.huzhurong.web.netty;

import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.bean.aware.Environment;
import top.huzhurong.ioc.bean.aware.EnvironmentAware;
import top.huzhurong.ioc.bean.aware.IocContainerAware;
import top.huzhurong.web.support.Server;

/**
 * init netty basic info ,  listen xxx port default 9652;
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
public class Nettyerver implements Server, EnvironmentAware, IocContainerAware {

    private static int POST = 9652;

    private Environment environment;

    private IocContainer iocContainer;

    @Override
    public void beforeStart() throws Exception {
        POST = (int) environment.getStringOrDef("port", POST);

    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setIocContainer(IocContainer iocContainer) {
        this.iocContainer = iocContainer;
    }
}
