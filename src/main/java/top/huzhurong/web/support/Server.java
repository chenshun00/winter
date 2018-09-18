package top.huzhurong.web.support;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/18
 */
public interface Server {

    void beforeStart() throws Exception;

    void start() throws Exception;

    void close() throws Exception;

}
