package top.huzhurong.web.support;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/18
 */
public interface Server {

    Server beforeStart();

    void start();

    void close();

}
