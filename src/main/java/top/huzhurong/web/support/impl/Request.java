package top.huzhurong.web.support.impl;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/21
 */
public interface Request {

    String getUri();

    String getPath();

    String getMethod();

    HttpSession getHttpSession();

}
