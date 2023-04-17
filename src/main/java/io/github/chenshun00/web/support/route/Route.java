package io.github.chenshun00.web.support.route;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/20
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Route {

    private Class<?> targetClass;
    private Object target;
    private Method method;
    private Map<String, Class<?>> parameters = new LinkedHashMap<>(8);
    private boolean json;
    private boolean body;
    private String mapping;
//    private List<HttpParam> httpParamList = new LinkedList<>();
}
