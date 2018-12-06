package top.huzhurong.event;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/12/6
 */
public interface EventListener {

    void init();

    void trigger();
}
