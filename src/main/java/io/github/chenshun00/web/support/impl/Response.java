package io.github.chenshun00.web.support.impl;

import io.github.chenshun00.web.support.http.HttpCookie;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Collection;

/**
 * @author chenshun00@gmail.com
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
