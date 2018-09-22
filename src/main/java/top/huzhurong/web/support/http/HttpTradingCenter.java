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
import top.huzhurong.web.asm.AsmParameterNameDiscover;
import top.huzhurong.web.asm.ParameterNameDiscoverer;
import top.huzhurong.web.support.Interceptor;
import top.huzhurong.web.support.impl.Response;
import top.huzhurong.web.support.impl.SimpleHttpRequest;
import top.huzhurong.web.support.impl.SimpleHttpResponse;
import top.huzhurong.web.support.route.HttpMatcher;
import top.huzhurong.web.support.route.Route;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
@Setter
public class HttpTradingCenter implements IocContainerAware, InitAware {

    private final static String JSON = "application/json;charset=utf-8";
    private final static String XML = "application/xml;charset=utf-8";
    private IocContainer iocContainer;
    private HttpMatcher httpMatcher;

    private List<Interceptor> interceptors;
    private final static String HTML = "text/html; charset=utf-8";
    private ParameterNameDiscoverer parameterNameDiscoverer = new AsmParameterNameDiscover();
    private HttpParameterParser httpParameterParser = new HttpParameterParser();

    private void toClient(ChannelHandlerContext ctx, HttpRequest request, Object object) {
        String json = JSONObject.toJSONString(Result.ofSuccess(object));
        ByteBuf byteBuf = Unpooled.copiedBuffer(json.getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        response(ctx, request, response, byteBuf);
    }

    private void serverError(ChannelHandlerContext ctx, HttpRequest request) {
        Result failed = Result.offail("Internal Server Error");
        String string = JSONObject.toJSONString(failed);
        ByteBuf byteBuf = Unpooled.copiedBuffer(string.getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, byteBuf);
        response(ctx, request, response, byteBuf);
    }

    private void methodError(ChannelHandlerContext ctx, HttpRequest request) {
        Result failed = Result.offail("Http Request Method Not Acceptable");
        String string = JSONObject.toJSONString(failed);
        ByteBuf byteBuf = Unpooled.copiedBuffer(string.getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_ACCEPTABLE, byteBuf);
        response(ctx, request, response, byteBuf);
    }

    private void notFound(ChannelHandlerContext ctx, HttpRequest request) {
        Result failed = Result.offail("Mapping Failed,Not Found");
        String string = JSONObject.toJSONString(failed);
        ByteBuf byteBuf = Unpooled.copiedBuffer(string.getBytes(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, byteBuf);
        response(ctx, request, response, byteBuf);
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

    /**
     * 根据method和uri，请求头，cookie，参数这些信息去找到最符合我们请求的一个数据
     *
     * @param httpRequest http 请求
     */
    public void handleRequest(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        SimpleHttpRequest request = SimpleHttpRequest.buildRequest(ctx, httpRequest);
        Response response = SimpleHttpResponse.buildResponse(ctx, request);

        Route route = httpMatcher.match(request);
        if (route == null) {
            Route blurryMatch = httpMatcher.blurryMatch(request);
            if (blurryMatch != null) {
                methodError(ctx, httpRequest);
            } else {
                notFound(ctx, httpRequest);
            }
            return;
        }

        Map<String, Object> paramMap;
        if (request.getMethod().equalsIgnoreCase("GET")) {
            paramMap = httpParameterParser.parseGetParams(httpRequest);
        } else {
            if (request.getMethod().equalsIgnoreCase("POST")
                    || request.getMethod().equalsIgnoreCase("PUT")
                    || request.getMethod().equalsIgnoreCase("DELETE")) {
                paramMap = httpParameterParser.parsetPostParams(ctx, httpRequest, response);
            } else {
                response.sendError(HttpResponseStatus.BAD_REQUEST, "不支持" + request.getMethod());
                return;
            }
        }
        request.setParams(paramMap);

        //response
        if (interceptors != null && interceptors.size() != 0) {
            for (Interceptor interceptor : interceptors) {
                if (!interceptor.preHandle(request, response)) {
                    response.sendError(HttpResponseStatus.BAD_REQUEST);
                    return;
                }
            }
        }

        Method method = route.getMethod();
        Object target = route.getTarget();
        Map<String, Class<?>> routeParameters = route.getParameters();

        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

        String requestMethod = request.getMethod();
        Object[] params = new Object[routeParameters.size()];
        //其实在这里可以做很多事的，例如时间戳转换，数据集合成对象，转型
        if (requestMethod.equalsIgnoreCase("GET")) {
            Map<String, Object> parameters = request.getParams();
            //实际参数遍历
            try {
                int i = 0;
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    Class<?> aClass = routeParameters.get(entry.getKey());

                    if (aClass == null) {
                        serverError(ctx, httpRequest);
                        return;
                    }

                    if (aClass.isAssignableFrom(Integer.class)) {
                        params[i] = Integer.parseInt((String) entry.getValue());
                    } else {
                        params[i] = aClass.cast(entry.getValue());
                    }
                    i++;
                }
            } catch (Exception e) {

            }

            try {
                Object invoke = method.invoke(target, params);

                if (interceptors != null && interceptors.size() != 0) {
                    for (Interceptor interceptor : interceptors) {
                        interceptor.postHandle(request, response);
                    }
                }

                if (route.isJson()) {
                    toClient(ctx, httpRequest, invoke);
                    return;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                serverError(ctx, httpRequest);
                return;
            }
        }

        try {
            Object invoke = method.invoke(target, params);

            if (route.isJson()) {
                ctx.channel().pipeline().addLast(new JsonObjectDecoder());
                toClient(ctx, httpRequest, invoke);
            } else {
                //将数据用html格式返回
                toClient(ctx, httpRequest, request);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            serverError(ctx, httpRequest);
        }
    }

    private void response(ChannelHandlerContext ctx, HttpRequest request, FullHttpResponse response, ByteBuf byteBuf) {
        boolean close = request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)
                || request.protocolVersion().equals(HttpVersion.HTTP_1_0)
                && !request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true);

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, JSON);
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
}
