package top.huzhurong.web.support.impl;

import top.huzhurong.web.support.http.HttpCookie;

import java.util.List;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/21
 */
public interface Request {

    String getUri();

    String getPath();

    String getMethod();

    HttpSession getHttpSession();

    void setHttpSession(HttpSession httpSession);

    List<HttpCookie> getHttpCookie();

}
