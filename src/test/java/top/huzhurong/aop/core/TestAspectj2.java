package top.huzhurong.aop.core;

import top.huzhurong.aop.annotation.After;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.annotation.Before;

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
}
