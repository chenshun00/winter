package top.huzhurong.ioc.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/6
 */
@Getter
@Setter
public class BeanInfo {
    private Class<?> aClass;
    private Object object;
    private String name;
    private boolean scope = true;

    public BeanInfo(Object object) {
        this(object, object.getClass().getSimpleName());
    }

    public BeanInfo(Object object, String name) {
        this(object.getClass(), object, name, true);
    }

    public BeanInfo(Class<?> aClass, Object object, String name, boolean scope) {
        this.aClass = aClass;
        this.object = object;
        this.name = name;
        this.scope = scope;
    }
}
