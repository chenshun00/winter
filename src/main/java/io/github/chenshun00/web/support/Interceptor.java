package io.github.chenshun00.web.support;

import io.github.chenshun00.web.support.impl.Request;
import io.github.chenshun00.web.support.impl.Response;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/21
 */
@FunctionalInterface
public interface Interceptor {

    default boolean preHandle(Request request, Response response) {
        return true;
    }


    void postHandle(Request request, Response response);

}
