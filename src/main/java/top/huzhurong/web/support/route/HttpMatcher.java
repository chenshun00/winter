package top.huzhurong.web.support.route;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import top.huzhurong.web.support.impl.SimpleHttpRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/20
 */
public class HttpMatcher {

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
        String path = httpRequest.getPath();
        String method = httpRequest.getMethod();
        String key = (path + "#" + method).toUpperCase();
        return routeMap.get(key);
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
