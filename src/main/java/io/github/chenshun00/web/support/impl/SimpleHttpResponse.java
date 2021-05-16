package io.github.chenshun00.web.support.impl;

import com.alibaba.fastjson.JSONObject;
import io.github.chenshun00.web.support.http.HttpCookie;
import io.github.chenshun00.web.support.http.HttpHeader;
import io.github.chenshun00.web.support.http.Result;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import lombok.Builder;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/22
 */
@Getter
@Builder
public class SimpleHttpResponse implements Response {

    private final static String JSON = "application/json;charset=utf-8";
    private final static String XML = "application/xml;charset=utf-8";
    private final static String HTML = "text/html; charset=utf-8";

    private List<HttpCookie> httpCookies;
    private Map<String, Object> httpHeaders;
    private Map<String, Object> other;
    private Integer statusCode;
    private SimpleHttpRequest simpleHttpRequest;
    private ChannelHandlerContext ctx;
    private HttpRequest httpRequest;

    public static Response buildResponse(ChannelHandlerContext ctx, SimpleHttpRequest simpleHttpRequest, HttpRequest httpRequest) {
        return SimpleHttpResponse.builder().ctx(ctx)
                .httpCookies(simpleHttpRequest.getHttpCookies())
                .httpHeaders(parseHeader(simpleHttpRequest))
                .simpleHttpRequest(simpleHttpRequest)
                .other(new HashMap<>())
                .httpRequest(httpRequest)
                .build();
    }

    private static Map<String, Object> parseHeader(SimpleHttpRequest simpleHttpRequest) {
        Map<String, Object> headers = new HashMap<>(16);
        List<HttpHeader> httpHeaders = simpleHttpRequest.getHttpHeaders();
        for (HttpHeader httpHeader : httpHeaders) {
            headers.put(httpHeader.getName(), httpHeader.getValue());
        }
        return headers;
    }

    @Override
    public void addCookie(HttpCookie cookie) {
        this.httpCookies.remove(cookie);
        this.httpCookies.add(cookie);
    }

    @Override
    public boolean containsHeader(String name) {
        if (this.httpHeaders.get(name) != null) {
            return this.httpHeaders.get(name) != null;
        } else if (this.other.get(name) != null) {
            return this.other.get(name) != null;
        } else {
            return false;
        }
    }

    @Override
    public void sendError(HttpResponseStatus responseStatus, Object msg) {
        setStatus(responseStatus.code());
        serverError(this.ctx, this.httpRequest, msg, responseStatus);
    }

    @Override
    public void sendError(HttpResponseStatus responseStatus) {
        setStatus(responseStatus.code());
    }

    @Override
    public void sendRedirect(String location) {
        setStatus(302);
    }

    @Override
    public void addDateHeader(String name, long date) {
        this.httpHeaders.remove(name);
        this.httpHeaders.put(name, date);

        this.other.remove(name);
        this.other.put(name, date);
    }

    @Override
    public void addHeader(String name, String value) {
        this.httpHeaders.remove(name);
        this.httpHeaders.put(name, value);

        this.other.remove(name);
        this.other.put(name, value);
    }

    @Override
    public void setStatus(int sc) {
        this.statusCode = sc;
    }

    @Override
    public Collection<String> getHeaderNames() {
        Set<String> set = this.httpHeaders.keySet();
        set.addAll(this.other.keySet());
        return set;
    }

    @Override
    public String getHeader(String name) {
        if (this.httpHeaders.get(name) != null) {
            return (String) this.httpHeaders.get(name);
        } else if (this.other.get(name) != null) {
            return (String) this.other.get(name);
        } else {
            return "";
        }
    }

    public void toClient(ChannelHandlerContext ctx, HttpRequest request, Object object) {
        String json = JSONObject.toJSONString(Result.ofSuccess(object));
        ByteBuf byteBuf = Unpooled.copiedBuffer(json.getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        response(ctx, request, response, byteBuf);
    }

    public void serverError(ChannelHandlerContext ctx, HttpRequest request, Object object, HttpResponseStatus responseStatus) {
        String string = JSONObject.toJSONString(object);
        ByteBuf byteBuf = Unpooled.copiedBuffer(string.getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, responseStatus, byteBuf);
        response(ctx, request, response, byteBuf);
    }

    public void methodError(ChannelHandlerContext ctx, HttpRequest request) {
        Result failed = Result.offail("Http Request Method Not Acceptable");
        String string = JSONObject.toJSONString(failed);
        ByteBuf byteBuf = Unpooled.copiedBuffer(string.getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_ACCEPTABLE, byteBuf);
        response(ctx, request, response, byteBuf);
    }

    public void notFound(ChannelHandlerContext ctx, HttpRequest request) {
        Result failed = Result.offail("Mapping Failed,Not Found");
        String string = JSONObject.toJSONString(failed);
        ByteBuf byteBuf = Unpooled.copiedBuffer(string.getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, byteBuf);
        response(ctx, request, response, byteBuf);
    }

    private void response(ChannelHandlerContext ctx, HttpRequest request, FullHttpResponse response, ByteBuf byteBuf) {

        boolean close = request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)
                || request.protocolVersion().equals(HttpVersion.HTTP_1_0)
                && !request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true);

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, JSON);
        if (!close) {
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
        }
        defaultHeader(response);
        //默认响应请求头
        for (Map.Entry<String, Object> entry : this.other.entrySet()) {
            response.headers().add(entry.getKey(), entry.getValue());
        }
        //添加的cookie加上
        Set<io.netty.handler.codec.http.cookie.Cookie> cookies = httpCookies.stream().map(httpCookie -> {
            Cookie cookie = new DefaultCookie(httpCookie.getName(), httpCookie.getValue());
            cookie.setDomain(httpCookie.getDomain());
            cookie.setHttpOnly(httpCookie.isHttpOnly());
            cookie.setMaxAge(httpCookie.getMaxAge());
            cookie.setPath(httpCookie.getPath());
            cookie.setSecure(httpCookie.isSecure());
            return cookie;
        }).collect(Collectors.toSet());

        //cookie转换成netty的cookie，写入
        if (!cookies.isEmpty()) {
            for (io.netty.handler.codec.http.cookie.Cookie cookie : cookies) {
                response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
            }
        }
        ChannelFuture future = ctx.channel().writeAndFlush(response);
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void defaultHeader(FullHttpResponse response) {
        response.headers().add("server", "winter");
    }
}
