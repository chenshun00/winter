package top.huzhurong.web.support.http;

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
            routeMap.putAll(toRoute(route));
        }
    }

    public Map<String, Route> toRoute(@NonNull Route route) {
        List<String> tag = route.getTag();
        if (tag.size() == 0) {
            throw new RuntimeException(route + "is illegal");
        }
        Map<String, Route> routeMap = new HashMap<>();
        tag.forEach(tt -> routeMap.put(tt, route));
        return routeMap;
    }

    public Route match(SimpleHttpRequest httpRequest) {
        String path = httpRequest.getPath();
        String method = httpRequest.getMethod();
        String key = (path + "#" + method).toUpperCase();
        return routeMap.get(key);
    }

}
