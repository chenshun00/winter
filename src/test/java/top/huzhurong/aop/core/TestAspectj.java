package top.huzhurong.aop.core;

import top.huzhurong.aop.annotation.After;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.annotation.Before;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/8/26
 */
@Aspectj
public class TestAspectj {

    @Before("info2")
    public void first() {
        System.out.println("我是TestAspectj的第1个测试方法");
    }

    @Before("info2")
    public void binfoefore() {
        System.out.println("before");
    }

    @Before("info2")
    public void inter() {
        System.out.println("我是TestAspectj的第二个测试方法");
    }

    @After("info2")
    public void after() {
        System.out.println("after");
    }
}
