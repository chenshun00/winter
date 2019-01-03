package top.huzhurong.web.asm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/22
 */
public interface ParameterNameDiscoverer {

    Map<String, String> getParameterNames(Method method);

    default List<String> getParameters(Method method) {
        return new ArrayList<>();
    }

}
