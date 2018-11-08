package top.huzhurong.web.support.session;

import top.huzhurong.aop.annotation.Order;
import top.huzhurong.ioc.annotation.Bean;
import top.huzhurong.web.support.Interceptor;
import top.huzhurong.web.support.http.HttpCookie;
import top.huzhurong.web.support.http.SessionManager;
import top.huzhurong.web.support.impl.HttpSession;
import top.huzhurong.web.support.impl.Request;
import top.huzhurong.web.support.impl.Response;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * session 过期策略
 *
 * @author luobo.cs@raycloud.com
 * @since 2018/11/8
 */
@Bean
@Order(value = 1)
public class SessionInterceptor implements Interceptor {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("1", "2", "3", "4", "5", "6");
        String s = list.stream().filter(x -> x.equals("7")).findAny().orElse(null);
        System.out.println(s);
    }

    @Override
    public boolean preHandle(Request request, Response response) {
        List<HttpCookie> httpCookies = request.getHttpCookie();
        HttpCookie httpCookie = httpCookies.stream()
                .filter(cookie -> cookie.getName().equals("JSESSION"))
                .findAny().orElse(null);
        //第一次
        if (httpCookie == null) {
            httpCookie = new HttpCookie("JSESSION", UUID.randomUUID().toString().replace("-", ""));
            response.addCookie(httpCookie);
        } else {
            //对应的cookie,根据cookie的值找到所有的属性，然后set到http session当中去
            String value = httpCookie.getValue();
            HttpSession httpSession = SessionManager.getHttpSession(value);
            //request.s
        }
        return true;
    }

    @Override
    public void postHandle(Request request, Response response) {

    }
}
