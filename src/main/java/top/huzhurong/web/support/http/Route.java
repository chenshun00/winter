package top.huzhurong.web.support.http;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/20
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Route {

    private RequestMethod[] requestMethods;
    private Class<?> targetClass;
    private Object target;
    private Method method;
    private Object[] params;
    private List<String> tag;
    private boolean json;

}
