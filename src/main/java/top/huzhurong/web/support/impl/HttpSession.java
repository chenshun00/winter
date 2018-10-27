package top.huzhurong.web.support.impl;

import java.util.Enumeration;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/10/27
 */
public interface HttpSession {
    long getCreationTime();

    String getId();

    Object getAttribute(String name);

    Enumeration<String> getAttributeNames();

    void setAttribute(String name, Object value);

    void removeAttribute(String name);

    boolean isNew();
}
