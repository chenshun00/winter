package top.huzhurong.web.support.http;

import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/24
 */
public class ControllerBean {
    private Map<Class<? extends Throwable>, Method> handle = new HashMap<>(16);


    public void addExceptionHandle(@NonNull Class<? extends Throwable> ex, @NonNull Method method) {
        this.handle.put(ex, method);
    }

    public Method getExceptionHandle(@NonNull Class<? extends Throwable> ex) {
        return this.handle.get(ex);
    }
}
