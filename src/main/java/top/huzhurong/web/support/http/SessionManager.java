package top.huzhurong.web.support.http;

import top.huzhurong.web.support.impl.HttpSession;
import top.huzhurong.web.support.impl.Request;
import top.huzhurong.web.support.impl.SimpleHttpSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/10/27
 */
public final class SessionManager {
    private static Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    public static void removeSession(Request request) {
        sessionMap.remove(request.getHttpSession().getId());
    }

    public static HttpSession getHttpSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public static String createHttpSession() {
        String sessionId = UUID.randomUUID().toString().replace("-", "");
        SimpleHttpSession httpSession;
        if (getHttpSession(sessionId) != null) {
            sessionId = UUID.randomUUID().toString().replace("-", "");
        }
        httpSession = new SimpleHttpSession();
        httpSession.setCreateTime(System.currentTimeMillis());
        httpSession.setId(sessionId);
        sessionMap.put(sessionId, httpSession);
        return sessionId;
    }

    public Map<String, Object> getSessionValue(String cookieValue) {
        return null;
    }

}
