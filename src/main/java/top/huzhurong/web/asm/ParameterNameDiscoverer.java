package top.huzhurong.web.asm;

import java.lang.reflect.Method;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/22
 */
public interface ParameterNameDiscoverer {

    String[] getParameterNames(Method method);

}
