package io.github.chenshun00.ioc.bean;

import java.util.Objects;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/6
 */
public class ClassInfo {
    private Class<?> aClass;
    private String className;


    public ClassInfo(Class<?> aClass, String className) {
        this.aClass = aClass;
        this.className = className;
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public void setaClass(Class<?> aClass) {
        this.aClass = aClass;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    @Override
    public String toString() {
        return "ClassInfo{" +
                "className='" + className + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ClassInfo)) return false;
        ClassInfo classInfo = (ClassInfo) object;
        return Objects.equals(getaClass(), classInfo.getaClass()) &&
                Objects.equals(getClassName(), classInfo.getClassName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getaClass(), getClassName());
    }
}
