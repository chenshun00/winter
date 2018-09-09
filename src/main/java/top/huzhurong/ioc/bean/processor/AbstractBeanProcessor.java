package top.huzhurong.ioc.bean.processor;

import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.ioc.annotation.Inject;
import top.huzhurong.ioc.bean.IocContainer;

import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/9
 */
public abstract class AbstractBeanProcessor implements BeanProcessor {

    @Inject
    private IocContainer iocContainer;

    @Override
    public Object processBeforeInit(Object object) {
        return object;
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
