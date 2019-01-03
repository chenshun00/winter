package top.huzhurong.web.support.impl;

import java.util.*;

/**
 * @author chenshun00@gmail.com
 * @since 2018/10/27
 */
public class SimpleHttpSession implements HttpSession {

    public long createTime;
    private String id;

    private Map<String, Object> attr = new HashMap<>();

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public long getCreationTime() {
        return createTime;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Object getAttribute(String name) {
        return attr.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        Set<String> names = new HashSet<>(attr.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public void setAttribute(String name, Object value) {
        attr.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        attr.remove(name);
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
