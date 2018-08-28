package top.huzhurong.aop.core;

import top.huzhurong.aop.annotation.After;
import top.huzhurong.aop.annotation.Around;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.annotation.Before;
import top.huzhurong.aop.invocation.Invocation;

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

    @Around("doInfo")
    public Object around(Invocation invocation) {
        System.out.println("jdk 环绕拦截前");
        try {
            Object proceed = invocation.proceed();
            System.out.println("jdk 环绕拦截后");
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
