package top.huzhurong.web.support.session;

import top.huzhurong.aop.annotation.Order;
import top.huzhurong.web.annotation.Filter;
import top.huzhurong.web.support.Interceptor;
import top.huzhurong.web.support.http.HttpCookie;
import top.huzhurong.web.support.http.SessionManager;
import top.huzhurong.web.support.impl.HttpSession;
import top.huzhurong.web.support.impl.Request;
import top.huzhurong.web.support.impl.Response;

import java.util.List;

/**
 * session 过期策略
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/11/8
 */
@Filter(order = 1)
@Order(value = 100)
public class SessionInterceptor implements Interceptor {

    @Override
    public boolean preHandle(Request request, Response response) {
        List<HttpCookie> httpCookies = request.getHttpCookie();
        HttpCookie httpCookie = httpCookies.stream()
                .filter(cookie -> cookie.getName().equals("JSESSION"))
                .findAny().orElse(null);
        //第一次
        String session;
        if (httpCookie == null) {
            //放入会话当中
            session = SessionManager.instance().createHttpSession(response);
        } else {
            //对应的cookie,根据cookie的值找到所有的属性，然后set到http session当中去
            session = httpCookie.getValue();
        }
        HttpSession httpSession = SessionManager.instance().getHttpSession(session);
        request.setHttpSession(httpSession);
        return true;
    }

    @Override
    public void postHandle(Request request, Response response) {

    }
}
