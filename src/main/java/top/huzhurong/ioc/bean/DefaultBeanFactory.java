package top.huzhurong.ioc.bean;

import top.huzhurong.ioc.annotation.Bean;
import top.huzhurong.ioc.annotation.Controller;

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
    public boolean register(Set<ClassInfo> classInfoSet) {
        classInfoSet.forEach(this::accept);
        return true;
    }

    @Override
    public List<String> getBeanNameForType(Class<?> type) {
        return iocContainer.getBeanNameForType(type);
    }

    private void accept(ClassInfo info) {
        Bean bean = info.getaClass().getAnnotation(Bean.class);
        Controller controller = info.getaClass().getAnnotation(Controller.class);
        if (bean != null && controller != null) {
            throw new IllegalStateException("Bean and Controller Cannot appear simultaneously at ---> "
                    + info.getaClass().getName());
        }
        String simpleName = info.getaClass().getSimpleName();
        simpleName = simpleName.substring(0, 1).toLowerCase() +
                simpleName.substring(1);
        if (bean != null) {
            doInput(bean.value().equals("") ? simpleName : bean.value(), info.getaClass());
            return;
        }
        if (controller != null) {
            doInput(controller.value().equals("") ? simpleName : controller.value(), info.getaClass());
            return;
        }
        doInput(simpleName, info.getaClass());
    }

    private void doInput(String name, Class<?> aClass) {
        try {
            iocContainer.put(name, aClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
