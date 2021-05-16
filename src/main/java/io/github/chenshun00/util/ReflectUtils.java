package io.github.chenshun00.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author chenshun00@gmail.com
 * @since 2018/10/9
 */
public class ReflectUtils {

    public static void setField(Field field, Object object, Object value) {
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static Object methodInvoke(Method method, Object object, Object[] args) {
        method.setAccessible(true);
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.getTargetException().printStackTrace();
        }
        return null;
    }
}
