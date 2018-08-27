package top.huzhurong.aop.core;

import top.huzhurong.aop.annotation.After;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.annotation.Before;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/27
 */
@Aspectj
public class TestAspectj3 {
    @Before("doInfo")
    public void before() {
        System.out.println("jdk 动态代理前置拦截");
    }

    @After("doInfo")
    public void after() {
        System.out.println("jdk 动态代理后置拦截");
    }
}
