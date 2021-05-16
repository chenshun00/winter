package io.github.chenshun00.web.support.impl;

import io.github.chenshun00.web.support.http.HttpCookie;
import io.github.chenshun00.web.support.http.HttpHeader;
import io.github.chenshun00.web.support.http.SessionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.github.chenshun00.util.WebUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/18
 */
@Getter
@Setter
@Builder
public class SimpleHttpRequest implements Request {

    private ChannelHandlerContext ctx;

    private HttpRequest httpRequest;

    private String method;

    private List<HttpHeader> httpHeaders;

    private List<HttpCookie> httpCookies;

    private String uri;

    private String path;

    private Map<String, Object> params;

    private String sessionId;

    //解析参数给route用
    public static SimpleHttpRequest buildRequest(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        return SimpleHttpRequest.builder().ctx(ctx).httpRequest(httpRequest)
//                .sessionId(SessionManager.createHttpSession())
                .method(httpRequest.method().name())
                .httpHeaders(parseHttpHeader(httpRequest))
                .httpCookies(parseCookie(httpRequest))
                .uri(httpRequest.uri())
                .path(parsePath(httpRequest))
                .build();
    }

    private static String parsePath(HttpRequest httpRequest) {
        QueryStringDecoder decoder = new QueryStringDecoder(httpRequest.uri());
        return decoder.path();
    }

    private static List<HttpHeader> parseHttpHeader(HttpRequest httpRequest) {
        List<HttpHeader> httpHeaders = new LinkedList<>();
        httpRequest.headers().forEach(header -> httpHeaders.add(new HttpHeader(header.getKey(), header.getValue())));
        return httpHeaders;
    }

    private static List<HttpCookie> parseCookie(HttpRequest httpRequest) {
        List<HttpCookie> cookieList = new LinkedList<>();
        String value = httpRequest.headers().get(HttpHeaderNames.COOKIE);
        if (value == null) {
            return cookieList;
        } else {
            Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(value);
            cookies.forEach(cookie -> {
                HttpCookie httpCookie = new HttpCookie(cookie.name(), cookie.value());
                httpCookie.setHttpOnly(cookie.isHttpOnly());
                httpCookie.setPath(cookie.path());
                cookieList.add(httpCookie);
            });
            return cookieList;
        }
    }

    public String getRemoteAddress() {
        return WebUtil.ip(this.ctx, this.httpRequest);
    }

    public String getProtocol() {
        return this.httpRequest.protocolVersion().protocolName();
    }

    @Override
    public HttpSession getHttpSession() {
        if (this.sessionId == null) {
            this.sessionId = SessionManager.instance().createHttpSession(null);
        }
        return SessionManager.instance().getHttpSession(this.sessionId);
    }

    @Override
    public void setHttpSession(HttpSession httpSession) {
        this.sessionId = httpSession.getId();
    }


    public List<HttpCookie> getHttpCookie() {
        return this.httpCookies;
    }
}
