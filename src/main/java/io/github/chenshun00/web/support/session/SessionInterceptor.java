package io.github.chenshun00.web.support.session;

import io.github.chenshun00.aop.annotation.Order;
import io.github.chenshun00.web.support.http.HttpCookie;
import io.github.chenshun00.web.support.http.SessionManager;
import io.github.chenshun00.web.support.impl.HttpSession;
import io.github.chenshun00.web.support.impl.Request;
import io.github.chenshun00.web.support.impl.Response;
import io.github.chenshun00.web.annotation.Filter;
import io.github.chenshun00.web.support.Interceptor;

import java.util.List;

/**
 * session 过期策略
 *
 * @author chenshun00@gmail.com
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
