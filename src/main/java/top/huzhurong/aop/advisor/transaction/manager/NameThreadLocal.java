package top.huzhurong.aop.advisor.transaction.manager;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/29
 */
public class NameThreadLocal<T> extends ThreadLocal<T> {
    private final String name;

    public NameThreadLocal(String name) {
        super();
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
