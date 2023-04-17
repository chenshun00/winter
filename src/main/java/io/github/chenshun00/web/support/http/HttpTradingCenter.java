package io.github.chenshun00.web.support.http;

import io.github.chenshun00.ioc.bean.IocContainer;
import io.github.chenshun00.ioc.bean.aware.InitAware;
import io.github.chenshun00.ioc.bean.aware.IocContainerAware;
import io.github.chenshun00.web.support.Interceptor;
import io.github.chenshun00.web.support.impl.Response;
import io.github.chenshun00.web.support.impl.SimpleHttpRequest;
import io.github.chenshun00.web.support.impl.SimpleHttpResponse;
import io.github.chenshun00.web.support.route.HttpMatcher;
import io.github.chenshun00.web.support.route.Route;
import io.github.chenshun00.web.support.upload.MultipartFile;
import io.github.chenshun00.web.support.upload.SimpleMultipartFile;
import io.github.chenshun00.web.util.AntPathMatcher;
import io.github.chenshun00.web.util.PathMatcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.multipart.FileUpload;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/19
 */
@Setter
@Slf4j
public class HttpTradingCenter implements IocContainerAware, InitAware {

    private IocContainer iocContainer;
    private HttpMatcher httpMatcher;

    private List<Interceptor> interceptors;
    private PathMatcher pathMatcher = new AntPathMatcher();
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
                paramMap = httpParameterParser.parsetPostParams(ctx, (FullHttpRequest) httpRequest, response);
                if (paramMap == null) {
                    return;
                }
            } else {
                response.sendError(HttpResponseStatus.BAD_REQUEST, "不支持" + request.getMethod());
                return;
            }
        }
        if (route.getMapping().contains("{") && route.getMapping().contains("}")) {
            Map<String, String> map = pathMatcher.extractUriTemplateVariables(route.getMapping(), request.getPath() + "#" + httpRequest.method());
            paramMap.putAll(map);
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
            //todo 如果entry是一个bean对象，那么应该需要迭代进去
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
            } else {
                ((SimpleHttpResponse) response).toClient(ctx, httpRequest, invoke.toString());
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            response.sendError(HttpResponseStatus.OK, ex.getMessage());
        } catch (InvocationTargetException ex) {
            Throwable targetException = ex.getTargetException();
            targetException.printStackTrace();
            if (this.controllerBean != null) {
                Method exceptionHandle = this.controllerBean.getExceptionHandle(targetException.getClass());
                if (exceptionHandle != null) {
                    Object invoke = exceptionHandle.invoke(this.iocContainer.getBean("controllerAdvice"), targetException);
                    response.sendError(HttpResponseStatus.OK, invoke);
                } else {
                    response.sendError(HttpResponseStatus.OK, targetException.getMessage());
                }
            } else {
                response.sendError(HttpResponseStatus.OK, targetException.getMessage());
            }
        } catch (Exception e) {
            log.error("出现异常", e);
            response.sendError(HttpResponseStatus.OK, e.getMessage());
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
        } else if (aClass.isAssignableFrom(MultipartFile.class)) {
            FileUpload fileUpload = (FileUpload) object;
            log.info("upload file name:{}", fileUpload.getFilename());
            param = new SimpleMultipartFile();
            ((SimpleMultipartFile) param).setFileUpload(fileUpload);
        } else if (aClass.isAssignableFrom(Date.class)) {
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
