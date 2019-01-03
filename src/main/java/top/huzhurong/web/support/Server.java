package top.huzhurong.web.support;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/18
 */
public interface Server {

    Server beforeStart();

    void start();

    void close();

}
