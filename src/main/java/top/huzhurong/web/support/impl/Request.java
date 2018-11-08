package top.huzhurong.web.support.impl;

import top.huzhurong.web.support.http.HttpCookie;

import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/21
 */
public interface Request {

    String getUri();

    String getPath();

    String getMethod();

    HttpSession getHttpSession();

    HttpSession setHttpSession(HttpSession httpSession);

    List<HttpCookie> getHttpCookie();

}
