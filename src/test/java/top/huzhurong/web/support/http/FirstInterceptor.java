package top.huzhurong.web.support.http;

import top.huzhurong.web.annotation.Filter;
import top.huzhurong.web.support.Interceptor;
import top.huzhurong.web.support.impl.Request;
import top.huzhurong.web.support.impl.Response;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/25
 */
@Filter
public class FirstInterceptor implements Interceptor {

    @Override
    public boolean preHandle(Request request, Response response) {
        System.out.println("FirstInterceptor preHandle");
        return true;
    }

    @Override
    public void postHandle(Request request, Response response) {
        System.out.println("FirstInterceptor  postHandle");
    }
}
