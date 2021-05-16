package io.github.chenshun00.ioc.bean.processor;

import io.github.chenshun00.ioc.bean.BeanFactory;
import io.github.chenshun00.ioc.bean.IocContainer;
import io.github.chenshun00.ioc.bean.aware.IocContainerAware;
import io.github.chenshun00.aop.advisor.Advisor;

import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/9
 */
public abstract class AbstractBeanProcessor implements BeanProcessor, IocContainerAware {

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
