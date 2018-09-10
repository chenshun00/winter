package top.huzhurong.ioc.scan.test.AspectjTest;

import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.annotation.Before;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/9/10
 */
@Aspectj
public class AspectjFirst {

    @Before("public void top.huzhurong.ioc.scan.test.TestScan1 hello()")
    public void before() {
        System.out.println("----* before TestScan1 ^v^ *----");
    }

}
