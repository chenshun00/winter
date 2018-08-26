package top.huzhurong.aop.invocation;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
public interface Invocation {

    Object proceed() throws Throwable;
}
