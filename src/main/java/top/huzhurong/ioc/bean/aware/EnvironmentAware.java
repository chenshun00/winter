package top.huzhurong.ioc.bean.aware;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/17
 */
public interface EnvironmentAware extends Aware {

    void setEnvironment(Environment environment);

}
