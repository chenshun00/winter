package top.huzhurong.ioc.bean.processor;

import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.ioc.annotation.Inject;
import top.huzhurong.ioc.bean.BeanFactory;
import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.bean.aware.IocContainerAware;

import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/9
 */
public abstract class AbstractBeanProcessor implements BeanProcessor, IocContainerAware {

    @Inject
    private IocContainer iocContainer;

    @Override
    public Object processAfterInit(Object object) {
        if (isInfrastructure(object)) {
            return object;
        }
        return processSubType(object);
    }

    protected abstract Object processSubType(Object object);


    protected boolean isInfrastructure(Object object) {
        return (object instanceof Advisor || object instanceof BeanProcessor || object instanceof IocContainer
                || object instanceof BeanFactory);
    }

    protected List<String> beanNameForType() {
        return iocContainer.getBeanNameForType(Advisor.class);
    }

    public IocContainer getIocContainer() {
        return iocContainer;
    }

    public void setIocContainer(IocContainer iocContainer) {
        this.iocContainer = iocContainer;
    }

}
