package top.huzhurong.web.support.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Builder;
import lombok.Getter;
import top.huzhurong.web.support.http.HttpCookie;
import top.huzhurong.web.support.http.HttpHeader;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/22
 */
@Getter
@Builder
public class SimpleHttpResponse implements Response {

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

    private void buildResponse() {
//        String connection = (String) this.httpHeaders.get(HttpHeaderNames.CONNECTION.toString());
//        keep-alive
//        boolean close = true;
//        if (connection != null && connection.equalsIgnoreCase(HttpHeaderValues.KEEP_ALIVE.toString())) {
//            close = false;
//        }
//        if (this.simpleHttpRequest.getProtocal().equalsIgnoreCase(HttpVersion.HTTP_1_0.toString())) {
//            close = true;
//        }
//        response.headers().set(HttpHeaderNames.CONTENT_TYPE, JSON);
//        if (!close) {
//            content-length
//            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
//        }
//        set cookie
//        Set<Cookie> cookies;
//        String value = request.headers().get(HttpHeaderNames.COOKIE);
//        if (value == null) {
//            cookies = Collections.emptySet();
//        } else {
//            cookies = ServerCookieDecoder.STRICT.decode(value);
//        }
//        if (!cookies.isEmpty()) {
//            for (Cookie cookie : cookies) {
//                response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
//            }
//        }
//        ChannelFuture future = ctx.channel().writeAndFlush(response);
//        if (close) {
//            future.addListener(ChannelFutureListener.CLOSE);
//        }
    }

}
