package top.huzhurong.ioc.bean;

import top.huzhurong.aop.advisor.Advisor;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.core.AspectjParser;
import top.huzhurong.aop.core.NameGenerator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/7
 */
public class DefaultIocContainer implements IocContainer {

    private Map<String, Object> context;
    private Map<String, BeanInfo> beanInfoMap;

    public DefaultIocContainer() {
        context = new ConcurrentHashMap<>(128);
        beanInfoMap = new ConcurrentHashMap<>(128);
    }

    @Override
    public Object getBean(String name) {
        return context.get(name);
    }

    @Override
    public <T> T getBean(final Class<T> tClass) {
        AtomicReference<String> name = new AtomicReference<>();
        beanInfoMap.forEach((key, value) -> {
            if (tClass.isAssignableFrom(value.getAClass())) {
                name.set(key);
            }
        });
        if (name.get() == null) {
            return null;
        } else {
            return tClass.cast(beanInfoMap.get(name.get()).getObject());
        }
    }

    @Override
    public <T> T getBean(String name, Class<T> tClass) {
        return tClass.cast(getBean(name));
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
    public List<String> getBeanNameForType(Class<?> tClass) {
        List<String> beanName = new LinkedList<>();
        context.forEach((key, value) -> {
            if (tClass.isAssignableFrom(value.getClass())) {
                beanName.add(key);
            }
        });
        return beanName;
    }

    @Override
    public void put(String name, Object object) {
        context.put(name, object);
        BeanInfo beanInfo = new BeanInfo(object, name);
        beanInfoMap.put(name, beanInfo);
    }

    public List<Object> aspectj() {
        List<Object> objects = new ArrayList<>();
        context.forEach((key, value) -> {
            if (value.getClass().getDeclaredAnnotation(Aspectj.class) != null) {
                objects.add(value);
            }
        });
        return objects;
    }


    private void accept(ClassInfo info) {
        try {
            Object instance = info.getaClass().newInstance();
            this.put(info.getClassName(), instance);
            if (info.getaClass().getAnnotation(Aspectj.class) != null) {
                List<Advisor> advisorList = AspectjParser.parserAspectj(info.getaClass(), instance);
                for (Advisor advisor : advisorList) {
                    this.put(NameGenerator.generator(advisor), advisor);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
