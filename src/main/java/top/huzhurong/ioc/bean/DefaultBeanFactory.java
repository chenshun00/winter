package top.huzhurong.ioc.bean;

import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.core.AspectjParser;
import top.huzhurong.aop.core.NameGenerator;

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
    public void put(String name, Object object) {
        this.iocContainer.put(name, object);
    }

    @Override
    public List<String> getBeanNameForType(Class<?> type) {
        return iocContainer.getBeanNameForType(type);
    }

    private void accept(ClassInfo info) {

        try {
            Object instance = info.getaClass().newInstance();
            iocContainer.put(info.getClassName(), instance);
            if (info.getaClass().getAnnotation(Aspectj.class) != null) {
                List<Advisor> advisorList = AspectjParser.parserAspectj(info.getaClass(), instance);
                for (Advisor advisor : advisorList) {
                    iocContainer.put(NameGenerator.generator(advisor), advisor);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
