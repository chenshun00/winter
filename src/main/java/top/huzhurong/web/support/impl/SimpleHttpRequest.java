package top.huzhurong.web.support.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import top.huzhurong.web.support.http.HttpCookie;
import top.huzhurong.web.support.http.HttpHeader;
import top.huzhurong.web.util.WebUtil;

import java.util.*;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/18
 */
@Getter
@Setter
@Builder
public class SimpleHttpRequest {

    private ChannelHandlerContext ctx;

    private HttpRequest httpRequest;

    private String method;

    private List<HttpHeader> httpHeaders;

    private List<HttpCookie> httpCookies;

    private String uri;

    private String path;

    private Map<String, Object> params;

    public static SimpleHttpRequest buildRequest(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        return SimpleHttpRequest.builder().ctx(ctx).httpRequest(httpRequest)
                .method(httpRequest.method().name())
                .httpHeaders(parseHttpHeader(httpRequest))
                .httpCookies(parseCookie(httpRequest))
                .uri(httpRequest.uri())
                .params(parseParams(httpRequest))
                .path(parsePath(httpRequest))
                .build();
    }

    private static String parsePath(HttpRequest httpRequest) {
        QueryStringDecoder decoder = new QueryStringDecoder(httpRequest.uri());
        return decoder.path();
    }

    private static Map<String, Object> parseParams(HttpRequest httpRequest) {
        String uri = httpRequest.uri();
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        Map<String, List<String>> parameters = decoder.parameters();
        Map<String, Object> parseParams = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (value.size() == 1) {
                parseParams.put(key, value.get(0));
            } else {
                parseParams.put(key, value);
            }
        });
        return parseParams;
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
                HttpCookie httpCookie = new HttpCookie();
                httpCookie.setName(cookie.name());
                httpCookie.setValue(cookie.value());
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

    public String protocolVersion() {
        return this.httpRequest.protocolVersion().protocolName();
    }

}
