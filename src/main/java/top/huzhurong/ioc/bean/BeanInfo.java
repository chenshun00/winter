package top.huzhurong.ioc.bean;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/6
 */
public class BeanInfo {
    private Class<?> aClass;
    private Object object;
    private String name;

    public BeanInfo(Object object) {
        this(object, object.getClass().getSimpleName());
    }

    public BeanInfo(Object object, String name) {
        this(object.getClass(), object, name);
    }

    public BeanInfo(Class<?> aClass, Object object, String name) {
        this.aClass = aClass;
        this.object = object;
        this.name = name;
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public void setaClass(Class<?> aClass) {
        this.aClass = aClass;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
