package io.github.chenshun00.aop.invocation;

/**
 * @author chenshun00@gmail.com
 * @since 2018/8/26
 */
public interface Invocation {

    Object proceed() throws Throwable;
}
