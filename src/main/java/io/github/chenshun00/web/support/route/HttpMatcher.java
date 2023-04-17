package io.github.chenshun00.web.support.route;

import io.github.chenshun00.web.support.impl.SimpleHttpRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import io.github.chenshun00.web.util.AntPathMatcher;
import io.github.chenshun00.web.util.PathMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/20
 */
public class HttpMatcher {

    private final PathMatcher pathMatcher = new AntPathMatcher();

    @Getter
    @Setter
    private Map<String, Route> routeMap = new HashMap<>();

    public void buildRouteMap(@NonNull List<Route> routeList) {
        for (Route route : routeList) {
            if (routeMap.get(route.getMapping()) != null) {
                throw new RuntimeException("Duplicate RequestMapping:" + route.getTargetClass().getName());
            } else {
                routeMap.put(route.getMapping(), route);
            }
        }
    }

    public Route match(SimpleHttpRequest httpRequest) {
        //精准匹配 path=/user/chenshun00/22 ---> /user/{name}/{age}
        String path = httpRequest.getPath();
        String method = httpRequest.getMethod();
        String key = (path + "#" + method);
        Route route = routeMap.get(key);

        //@PathVariable注解匹配
        if (route == null) {
            for (Map.Entry<String, Route> entry : routeMap.entrySet()) {
                String kk = entry.getKey();
                if (pathMatcher.match(kk, key)) {
                    return entry.getValue();
                }
            }
        }
        return route;
    }

    public Route blurryMatch(SimpleHttpRequest httpRequest) {
        String path = httpRequest.getPath();
        for (Map.Entry<String, Route> entry : routeMap.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(path)) {
                return entry.getValue();
            }
        }
        return null;
    }

}
