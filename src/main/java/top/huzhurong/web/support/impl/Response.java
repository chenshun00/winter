package top.huzhurong.web.support.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import top.huzhurong.web.support.http.HttpCookie;

import java.util.Collection;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/21
 */
public interface Response {

    void addCookie(HttpCookie cookie);

    boolean containsHeader(String name);

    void sendError(HttpResponseStatus responseStatus, Object msg);

    void sendError(HttpResponseStatus responseStatus);

    void sendRedirect(String location);

    void addDateHeader(String name, long date);

    void addHeader(String name, String value);

    void setStatus(int sc);

    Collection<String> getHeaderNames();

    String getHeader(String name);
}
