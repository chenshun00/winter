package top.huzhurong.ioc.bean;

import top.huzhurong.aop.annotation.Aspectj;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/7
 */
public class DefaultIocContainer implements IocContainer {

    /**
     * name,
     */
    private Map<String, Object> context;
    private Map<Class<?>, Object> classContext;
    private Map<String, Object> ignore;

    public DefaultIocContainer() {
        context = new ConcurrentHashMap<>(128);
        classContext = new ConcurrentHashMap<>(128);
    }

    @Override
    public Object getBean(String name) {
        return context.get(name);
    }

    @Override
    public <T> T getBean(Class<T> tClass) {
        return tClass.cast(classContext.get(tClass));
    }

    @Override
    public <T> T getBean(String name, Class<T> tClass) {
        return tClass.cast(getBean(name));
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
        classContext.put(object.getClass(), object);
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

}
