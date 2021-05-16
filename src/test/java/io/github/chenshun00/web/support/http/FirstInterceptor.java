package io.github.chenshun00.web.support.http;

import io.github.chenshun00.web.support.impl.Response;
import io.github.chenshun00.web.support.Interceptor;
import io.github.chenshun00.web.support.impl.Request;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/25
 */
//@Filter
//@Order(3)
public class FirstInterceptor implements Interceptor {

    @Override
    public boolean preHandle(Request request, Response response) {
        System.out.println("FirstInterceptor preHandle");
        request.getHttpSession().setAttribute("chenshun", "chenshun00");
        return true;
    }

    @Override
    public void postHandle(Request request, Response response) {
        System.out.println("FirstInterceptor  postHandle");
    }
}
