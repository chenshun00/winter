package top.huzhurong.ioc.scan.test.AspectjTest;

import top.huzhurong.aop.annotation.After;
import top.huzhurong.aop.annotation.Aspectj;
import top.huzhurong.aop.annotation.Before;
import top.huzhurong.aop.annotation.Order;

/**
 * @author chenshun00@gmail.com
 * @since 2018/9/10
 */
@Aspectj
@Order(1)
public class AspectjSecond {
    @Before("public void top.huzhurong.ioc.scan.test.TestScan1 hello()")
    public void before() {
        System.out.println("----* v^v AspectjSecond aspectj before TestScan1 v^v *----");
    }


    @After("public void top.huzhurong.ioc.scan.test.TestScan1 hello()")
    public void after() {
        System.out.println("----* ^@^ AspectjSecond aspectj after TestScan1 ^@^ *----");
    }
}
