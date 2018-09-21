package top.huzhurong.web.support;

import top.huzhurong.web.support.impl.Request;
import top.huzhurong.web.support.impl.Response;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/21
 */
@FunctionalInterface
public interface Interceptor {

    default boolean preHandle(Request request, Response response) {
        return true;
    }


    void postHandle(Request request, Response response);

}
