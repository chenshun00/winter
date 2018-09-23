package top.huzhurong.web.support.impl;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import lombok.Builder;
import lombok.Getter;
import top.huzhurong.web.support.http.HttpCookie;
import top.huzhurong.web.support.http.HttpHeader;
import top.huzhurong.web.support.http.Result;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author luobo.cs@raycloud.com
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
    private Integer statusCode = 200;
    private SimpleHttpRequest simpleHttpRequest;
    private ChannelHandlerContext ctx;

    public static Response buildResponse(ChannelHandlerContext ctx, SimpleHttpRequest simpleHttpRequest) {
        return SimpleHttpResponse.builder().ctx(ctx)
                .httpCookies(simpleHttpRequest.getHttpCookies())
                .httpHeaders(parseHeader(simpleHttpRequest))
                .simpleHttpRequest(simpleHttpRequest)
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
        return this.httpHeaders.get(name) != null;
    }

    @Override
    public void sendError(HttpResponseStatus responseStatus, String msg) {
        setStatus(responseStatus.code());
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
    }

    @Override
    public void addHeader(String name, String value) {
        this.httpHeaders.remove(name);
        this.httpHeaders.put(name, value);
    }

    @Override
    public void setStatus(int sc) {
        this.statusCode = sc;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return this.httpHeaders.keySet();
    }

    @Override
    public String getHeader(String name) {
        return (String) this.httpHeaders.get(name);
    }

    public void toClient(ChannelHandlerContext ctx, HttpRequest request, Object object) {
        String json = JSONObject.toJSONString(Result.ofSuccess(object));
        ByteBuf byteBuf = Unpooled.copiedBuffer(json.getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        response(ctx, request, response, byteBuf);
    }

    public void serverError(ChannelHandlerContext ctx, HttpRequest request) {
        Result failed = Result.offail("Internal Server Error");
        String string = JSONObject.toJSONString(failed);
        ByteBuf byteBuf = Unpooled.copiedBuffer(string.getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, byteBuf);
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
        Set<io.netty.handler.codec.http.cookie.Cookie> cookies;
        String value = request.headers().get(HttpHeaderNames.COOKIE);
        if (value == null) {
            cookies = Collections.emptySet();
        } else {
            cookies = ServerCookieDecoder.STRICT.decode(value);
        }
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

}
