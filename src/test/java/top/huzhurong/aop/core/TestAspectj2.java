package top.huzhurong.aop.core;

import top.huzhurong.aop.annotation.After;
import top.huzhurong.aop.annotation.Around;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.annotation.Before;
import top.huzhurong.aop.invocation.Invocation;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
@Aspectj
public class TestAspectj2 {
    @Before("info2")
    public void binfoefore2() {
        System.out.println("before2");
    }

    @After("info2")
    public void after() {
        System.out.println("after2");
    }

    /**
     * 测试带参数 Invocation invocation
     */
    @Around("other")
    public Object around(Invocation invocation) throws Throwable {
        System.out.println("around before");
        Object proceed = invocation.proceed();
        System.out.println("around after");
        return proceed;
    }
    /**
     * 测试不带参数，建议分开参数
     */
//    @Around("other")
//    public Object around() throws Throwable {
//        System.out.println("around before");
////        Object proceed = invocation.proceed();
//        System.out.println("around after");
//        return "1";
//    }
}
