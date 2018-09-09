package top.huzhurong.ioc.bean;

import java.util.List;
import java.util.Set;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/6
 */
public class DefaultBeanFactory implements InfoBeanFactory {

    private IocContainer iocContainer;

    public DefaultBeanFactory(IocContainer iocContainer) {
        this.iocContainer = iocContainer;
    }

    @Override
    public Object getBean(String name) {
        return iocContainer.getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> tClass) {
        return iocContainer.getBean(tClass);
    }

    @Override
    public <T> T getBean(String name, Class<T> tClass) {
        return iocContainer.getBean(name, tClass);
    }

    @Override
    public Set<ClassInfo> register(Set<ClassInfo> classInfoSet) {
        classInfoSet.forEach(this::accept);
        return classInfoSet;
    }

    @Override
    public void register(ClassInfo classInfo) {
        this.accept(classInfo);
    }

    @Override
    public List<String> getBeanNameForType(Class<?> type) {
        return iocContainer.getBeanNameForType(type);
    }

    private void accept(ClassInfo info) {
        try {
            iocContainer.put(info.getClassName(), info.getaClass().newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
