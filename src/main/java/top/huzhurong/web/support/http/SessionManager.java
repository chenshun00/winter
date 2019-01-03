package top.huzhurong.web.support.http;

import lombok.Getter;
import lombok.Setter;
import top.huzhurong.web.support.impl.HttpSession;
import top.huzhurong.web.support.impl.Response;
import top.huzhurong.web.support.impl.SimpleHttpSession;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenshun00@gmail.com
 * @since 2018/10/27
 */
public final class SessionManager {

    public final static SessionManager SESSION_MANAGER = new SessionManager();
    private static long DEFAULT_EXPIRE_TIME = 1000 * 1000 * 60 * 30;
    private final Map<String, CacheSession> sessionMap = new ConcurrentHashMap<>();

    private SessionManager() {
        new Thread(() -> {
            while (true) {
                System.out.println(sessionMap.size());
                if (sessionMap.size() == 0) {
                    return;
                }
                synchronized (sessionMap) {
                    List<String> strings = new LinkedList<>();
                    sessionMap.forEach((k, v) -> {
                        if ((System.currentTimeMillis() - v.getExpireTime()) > DEFAULT_EXPIRE_TIME) {
                            strings.add(k);
                        }
                    });
                    strings.forEach(sessionMap::remove);
                }

                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static SessionManager instance() {
        return SessionManagerHolder.manager();
    }

    public HttpSession getHttpSession(String sessionId) {
        CacheSession cacheSession = sessionMap.get(sessionId);
        if (cacheSession == null) {
            return null;
        }
        return cacheSession.getHttpSession();
    }

    public String createHttpSession(Response response) {
        String sessionId = UUID.randomUUID().toString().replace("-", "");
        SimpleHttpSession httpSession;
        if (getHttpSession(sessionId) != null) {
            sessionId = UUID.randomUUID().toString().replace("-", "");
        }
        httpSession = new SimpleHttpSession();
        httpSession.setCreateTime(System.currentTimeMillis());
        httpSession.setId(sessionId);
        CacheSession cacheSession = new CacheSession(System.currentTimeMillis() + DEFAULT_EXPIRE_TIME, httpSession);
        sessionMap.put(sessionId, cacheSession);

        if (response != null) {
            HttpCookie httpCookie = new HttpCookie("JSESSION", sessionId);
            response.addCookie(httpCookie);
        }

        return sessionId;
    }

    @Getter
    @Setter
    private static class CacheSession {
        private Long expireTime;
        private HttpSession httpSession;

        public CacheSession(Long expireTime, HttpSession httpSession) {
            this.expireTime = expireTime;
            this.httpSession = httpSession;
        }
    }

    private static class SessionManagerHolder {
        public static SessionManager manager() {
            return SESSION_MANAGER;
        }
    }
}
