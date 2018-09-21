package top.huzhurong.web.support.http;

import top.huzhurong.aop.core.StringUtil;
import top.huzhurong.web.annotation.Json;
import top.huzhurong.web.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/20
 */
public class HttpRouteBuilder {

    public List<Route> buildRoute(Object instance) {
        List<Route> routeList = new LinkedList<>();
        RequestMapping requestMapping = instance.getClass().getDeclaredAnnotation(RequestMapping.class);
        Method[] declaredMethods = instance.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (Modifier.isPublic(declaredMethod.getModifiers()) && !Modifier.isStatic(declaredMethod.getModifiers())) {
                RequestMapping declaredAnnotation = declaredMethod.getDeclaredAnnotation(RequestMapping.class);
                if (declaredAnnotation != null) {
                    Json json = declaredMethod.getDeclaredAnnotation(Json.class);
                    RequestMethod[] requestMethods = declaredAnnotation.method();

                    List<String> tags = new LinkedList<>();

                    String parent = requestMapping.value();
                    if (parent.trim().length() != 0 && !parent.startsWith("/")) {
                        parent = "/" + parent;
                    }

                    String child = declaredAnnotation.value();
                    if (StringUtil.containSpace(child)) {
                        throw new RuntimeException("class:" + instance.getClass().getName() +
                                "--RequestMapping#value can't be null or contain writeSpace");
                    }
                    if (!child.startsWith("/")) {
                        child = "/" + child;
                    }
                    String key = parent + child;
                    if (requestMethods.length == 0) {
                        tags.add((key + "#" + "post").toUpperCase());
                        tags.add((key + "#" + "get").toUpperCase());
                        tags.add((key + "#" + "put").toUpperCase());
                        tags.add((key + "#" + "delete").toUpperCase());
                    } else {
                        for (RequestMethod requestMethod : requestMethods) {
                            String name = requestMethod.name();
                            String tag = (key + "#" + name).toUpperCase();
                            tags.add(tag);
                        }
                    }

                    for (String tag : tags) {
                        Route route = new Route();
                        route.setJson(json != null);
                        route.setTargetClass(instance.getClass());
                        route.setMethod(declaredMethod);
                        route.setTarget(instance);
                        route.setMapping(tag);
                        routeList.add(route);
                    }


                }
            }
        }
        return routeList;
    }

}
