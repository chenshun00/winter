package top.huzhurong.web.support.http;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import lombok.Setter;
import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.bean.aware.InitAware;
import top.huzhurong.ioc.bean.aware.IocContainerAware;
import top.huzhurong.web.support.Interceptor;
import top.huzhurong.web.support.impl.SimpleHttpRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
@Setter
public class HttpTradingCenter implements IocContainerAware, InitAware {

    private final static String EMPTY = " ";

    private IocContainer iocContainer;
    private HttpMatcher httpMatcher;

    private List<Interceptor> interceptors;

    /**
     * 根据method和uri，请求头，cookie，参数这些信息去找到最符合我们请求的一个数据
     *
     * @param httpRequest http 请求
     */
    public void handleRequest(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        SimpleHttpRequest simpleHttpRequest = SimpleHttpRequest.buildRequest(ctx, httpRequest);

        Route route = httpMatcher.match(simpleHttpRequest);
        if (route == null) {
            notFound(ctx, httpRequest, simpleHttpRequest);
            return;
        }

        if (interceptors != null && interceptors.size() != 0) {
            //todo 等待实现
        }

        Method method = route.getMethod();
        Object target = route.getTarget();
        Object[] params = route.getParams();
        try {
            Object invoke = method.invoke(target, params);
            if (route.isJson()) {
                ctx.channel().pipeline().addLast(new JsonObjectDecoder());
                String string = JSONObject.toJSONString(invoke);
                toClient(ctx, httpRequest, simpleHttpRequest);
            } else {
                //将数据用html格式返回
                toClient(ctx, httpRequest, simpleHttpRequest);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            serverError(ctx, httpRequest, simpleHttpRequest);
        }
    }

    private void toClient(ChannelHandlerContext ctx, HttpRequest httpRequest, SimpleHttpRequest simpleHttpRequest) {

    }

    private void serverError(ChannelHandlerContext ctx, HttpRequest request, SimpleHttpRequest simpleHttpRequest) {

    }

    private void notFound(ChannelHandlerContext ctx, HttpRequest request, SimpleHttpRequest simpleHttpRequest) {
        String builder = simpleHttpRequest.getProtocal() + EMPTY +
                404 + EMPTY + "Not Found";
        ByteBuf byteBuf = Unpooled.copiedBuffer(builder.getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);


        boolean close = request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)
                || request.protocolVersion().equals(HttpVersion.HTTP_1_0)
                && !request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true);

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        if (!close) {
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
        }
        Set<Cookie> cookies;
        String value = request.headers().get(HttpHeaderNames.COOKIE);
        if (value == null) {
            cookies = Collections.emptySet();
        } else {
            cookies = ServerCookieDecoder.STRICT.decode(value);
        }
        if (!cookies.isEmpty()) {
            for (Cookie cookie : cookies) {
                response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
            }
        }
        ChannelFuture future = ctx.channel().writeAndFlush(response);
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }


    @Override
    public void setIocContainer(IocContainer iocContainer) {
        this.iocContainer = iocContainer;
        this.httpMatcher = this.iocContainer.getBean(HttpMatcher.class);
        this.interceptors = this.iocContainer.getBeanInstancesForType(Interceptor.class);
    }

    @Override
    public void initBean() {
        if (httpMatcher == null) {
            throw new RuntimeException("httpMatcher can't be null");
        }
    }
}
