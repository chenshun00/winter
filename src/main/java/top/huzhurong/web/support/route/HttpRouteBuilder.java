package top.huzhurong.web.support.route;

import top.huzhurong.aop.core.StringUtil;
import top.huzhurong.web.annotation.Json;
import top.huzhurong.web.annotation.RequestMapping;
import top.huzhurong.web.asm.AsmParameterNameDiscover;
import top.huzhurong.web.asm.ParameterNameDiscoverer;
import top.huzhurong.web.support.http.RequestMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/20
 */
public class HttpRouteBuilder {

    private static final Map<String, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(8);

    static {
        primitiveWrapperTypeMap.put("boolean", Boolean.class);
        primitiveWrapperTypeMap.put("int", Integer.class);
        primitiveWrapperTypeMap.put("char", Character.class);
        primitiveWrapperTypeMap.put("double", Double.class);
        primitiveWrapperTypeMap.put("float", Float.class);
        primitiveWrapperTypeMap.put("byte", Byte.class);
        primitiveWrapperTypeMap.put("long", Long.class);
        primitiveWrapperTypeMap.put("short", Short.class);
    }

    private ParameterNameDiscoverer parameterNameDiscoverer = new AsmParameterNameDiscover();

    /**
     * 基本类型靠parse，自定义类型，就只能反射set了
     */
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
                        Map<String, Class<?>> routeParameters = route.getParameters();
                        Map<String, String> parameterNames = parameterNameDiscoverer.getParameterNames(declaredMethod);
                        for (Map.Entry<String, String> entry : parameterNames.entrySet()) {
                            Class<?> aClass;
                            if ((aClass = primitiveWrapperTypeMap.get(entry.getKey())) == null) {
                                try {
                                    aClass = Class.forName(entry.getValue());
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            routeParameters.put(entry.getKey(), aClass);
                        }

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
