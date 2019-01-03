package top.huzhurong.ioc.scan.test.AspectjTest;

import top.huzhurong.aop.annotation.After;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.annotation.Before;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/10
 */
@Aspectj
public class AspectjFirst {

    @Before("public void top.huzhurong.ioc.scan.test.TestScan1 gg()")
    public void before() {
        System.out.println("----* ^v^ AspectjFirst aspectj before TestScan1 ^v^ *----");
    }


    @After("public void top.huzhurong.ioc.scan.test.TestScan1 gg()")
    public void after() {
        System.out.println("----* ^8^ AspectjFirst aspectj after TestScan1 ^8^ *----");
    }

}
