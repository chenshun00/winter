package top.huzhurong.web.support.http;

import top.huzhurong.web.support.Interceptor;
import top.huzhurong.web.support.impl.Request;
import top.huzhurong.web.support.impl.Response;

/**
 * @author luobo.cs@raycloud.com
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
