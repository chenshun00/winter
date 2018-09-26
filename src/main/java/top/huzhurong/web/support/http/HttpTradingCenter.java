package top.huzhurong.web.support.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Setter;
import top.huzhurong.ioc.bean.IocContainer;
import top.huzhurong.ioc.bean.aware.InitAware;
import top.huzhurong.ioc.bean.aware.IocContainerAware;
import top.huzhurong.web.support.Interceptor;
import top.huzhurong.web.support.impl.Response;
import top.huzhurong.web.support.impl.SimpleHttpRequest;
import top.huzhurong.web.support.impl.SimpleHttpResponse;
import top.huzhurong.web.support.route.HttpMatcher;
import top.huzhurong.web.support.route.Route;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/19
 */
@Setter
public class HttpTradingCenter implements IocContainerAware, InitAware {

    private IocContainer iocContainer;
    private HttpMatcher httpMatcher;

    private List<Interceptor> interceptors;
    private HttpParameterParser httpParameterParser = new HttpParameterParser();
    private ControllerBean controllerBean;

    @Override
    public void setIocContainer(IocContainer iocContainer) {
        this.iocContainer = iocContainer;
        this.httpMatcher = this.iocContainer.getBean(HttpMatcher.class);
        this.interceptors = this.iocContainer.getBeanInstancesForType(Interceptor.class);

        this.controllerBean = this.iocContainer.getBean(ControllerBean.class);
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
    public void handleRequest(ChannelHandlerContext ctx, HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException {
        SimpleHttpRequest request = SimpleHttpRequest.buildRequest(ctx, httpRequest);
        Response response = SimpleHttpResponse.buildResponse(ctx, request, httpRequest);

        //精准匹配，url和route直接映射
        Route route = httpMatcher.match(request);
        if (route == null) {
            Route blurryMatch = httpMatcher.blurryMatch(request);
            if (blurryMatch != null) {
                ((SimpleHttpResponse) response).methodError(ctx, httpRequest);
            } else {
                ((SimpleHttpResponse) response).notFound(ctx, httpRequest);
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
        Object[] params = new Object[routeParameters.size()];
        Map<String, Object> parameters = request.getParams();
        int i = 0;
        for (Map.Entry<String, Class<?>> entry : routeParameters.entrySet()) {
            Class<?> aClass = routeParameters.get(entry.getKey());
            params[i++] = parseParam(aClass, parameters.get(entry.getKey()), request, response);
        }

        try {
            Object invoke = method.invoke(target, params);
            if (interceptors != null && interceptors.size() != 0) {
                for (Interceptor interceptor : interceptors) {
                    interceptor.postHandle(request, response);
                }
            }

            if (route.isJson()) {
                ((SimpleHttpResponse) response).toClient(ctx, httpRequest, invoke);
            }
        } catch (IllegalAccessException ex) {
            response.sendError(HttpResponseStatus.OK, ex.getMessage());
        } catch (InvocationTargetException ex) {
            Throwable targetException = ex.getTargetException();
            if (this.controllerBean != null) {
                Method exceptionHandle = this.controllerBean.getExceptionHandle(targetException.getClass());
                if (exceptionHandle != null) {
                    Object invoke = exceptionHandle.invoke(this.iocContainer.getBean("controllerAdvice"), targetException);
                    response.sendError(HttpResponseStatus.OK, invoke);
                }
            } else {
                response.sendError(HttpResponseStatus.OK, targetException.getMessage());
            }
        }
    }

    private Object parseParam(Class<?> aClass, Object object, SimpleHttpRequest request, Response response) {
        Object param = null;
        if (aClass.isAssignableFrom(Integer.class)) {
            param = Integer.parseInt((String) object);
        } else if (aClass.isAssignableFrom(Byte.class)) {
            param = Byte.parseByte((String) object);
        } else if (aClass.isAssignableFrom(Short.class)) {
            param = Short.parseShort((String) object);
        } else if (aClass.isAssignableFrom(Float.class)) {
            param = Float.parseFloat((String) object);
        } else if (aClass.isAssignableFrom(Double.class)) {
            param = Double.parseDouble((String) object);
        } else if (aClass.isAssignableFrom(Long.class)) {
            param = Long.parseLong((String) object);
        } else if (aClass.isAssignableFrom(String.class)) {
            param = aClass.cast(object);
        } else if (aClass.isAssignableFrom(SimpleHttpRequest.class)) {
            param = request;
        } else if (aClass.isAssignableFrom(Response.class)) {
            param = response;
        } else //Date date = (Date) aClass.newInstance();
            if (aClass.isAssignableFrom(Date.class)) {
                param = object;
            } else {
                try {
                    param = aClass.newInstance();
                    Map<String, Object> params = request.getParams();
                    Field[] declaredFields = param.getClass().getDeclaredFields();
                    for (Field declaredField : declaredFields) {
                        String name = declaredField.getName();
                        if (params.get(name) != null) {
                            declaredField.setAccessible(true);
                            declaredField.set(param, priai(declaredField.getType(), params.get(name)));
                        }
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        return param;
    }


    private Object priai(Class<?> aClass, Object object) {
        Object param;
        if (aClass.isAssignableFrom(Integer.class)) {
            param = Integer.parseInt((String) object);
        } else if (aClass.isAssignableFrom(Byte.class)) {
            param = Byte.parseByte((String) object);
        } else if (aClass.isAssignableFrom(Short.class)) {
            param = Short.parseShort((String) object);
        } else if (aClass.isAssignableFrom(Float.class)) {
            param = Float.parseFloat((String) object);
        } else if (aClass.isAssignableFrom(Double.class)) {
            param = Double.parseDouble((String) object);
        } else if (aClass.isAssignableFrom(Long.class)) {
            param = Long.parseLong((String) object);
        } else if (aClass.isAssignableFrom(String.class)) {
            param = object;
        } else {
            return null;
        }
        return param;
    }
}
