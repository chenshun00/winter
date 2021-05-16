package io.github.chenshun00.ioc.bean.processor;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * @author chenshun00@gmail.com
 * @since 2018/10/9
 */
@Getter
@Setter
public class MethodObject {
    private Method method;
    private Object object;
}
